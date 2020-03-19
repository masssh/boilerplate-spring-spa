package masssh.boilerplate.spring.spa.dao.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.interceptor.MyBatisUpdateInterceptor;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
@Configuration
@MapperScan("masssh.boilerplate.spring.spa.dao")
@RequiredArgsConstructor
public class DatabaseConfiguration {
    private final ApplicationProperty applicationProperty;

    @Bean
    public DataSource dataSource() {
        final ApplicationProperty.MysqlProperty dataSourceProperty = applicationProperty.getMysql();
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
    public SqlSessionFactoryBean sqlSessionFactoryBean(final DataSource dataSource) throws IOException {
        final ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeHandlersPackage("masssh.boilerplate.spring.spa.dao.typehandler");
        sqlSessionFactoryBean.setTypeAliasesPackage("masssh.boilerplate.spring.spa.model.row");
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        final org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setUseGeneratedKeys(true);
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.WARNING);
        configuration.setDefaultStatementTimeout(60);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.addInterceptor(new MyBatisUpdateInterceptor());
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
