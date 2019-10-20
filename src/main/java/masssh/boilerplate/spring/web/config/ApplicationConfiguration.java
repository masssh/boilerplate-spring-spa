package masssh.boilerplate.spring.web.config;

import com.zaxxer.hikari.HikariDataSource;
import masssh.boilerplate.spring.web.config.property.ApplicationProperty;
import masssh.boilerplate.spring.web.config.property.ApplicationProperty.MysqlProperty;
import masssh.boilerplate.spring.web.config.property.ApplicationProperty.RedisProperty;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import javax.sql.DataSource;

@MapperScan(basePackages = "masssh.boilerplate.spring.web")
@Configuration
public class ApplicationConfiguration {
    @Bean
    public DataSource dataSource(final ApplicationProperty applicationProperty) {
        final MysqlProperty dataSourceProperty = applicationProperty.getMysql();
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceProperty.getJdbcUrl());
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setPoolName(dataSourceProperty.getPoolName());
        dataSource.setMinimumIdle(dataSourceProperty.getMinimumIdle());
        dataSource.setMaximumPoolSize(dataSourceProperty.getMaximumPoolSize());
        dataSource.setLeakDetectionThreshold(dataSourceProperty.getLeakDetectionThreshold());
        return dataSource;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(final ApplicationProperty applicationProperty) {
        final RedisProperty redisProperty = applicationProperty.getRedis();
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(
                redisProperty.getHost(),
                redisProperty.getPort()
        ));
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(final DataSource dataSource) {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeHandlersPackage("masssh.boilerplate.spring.web.dao.typehandler");
        sqlSessionFactoryBean.setTypeAliasesPackage("masssh.boilerplate.spring.web.row");
        final org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setUseGeneratedKeys(true);
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.WARNING);
        configuration.setDefaultStatementTimeout(60);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
    }
}
