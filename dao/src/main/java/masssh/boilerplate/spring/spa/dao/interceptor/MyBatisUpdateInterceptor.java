package masssh.boilerplate.spring.spa.dao.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
@Slf4j
public class MyBatisUpdateInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        final Object parameter = args[1];
        if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
            onInsert(parameter);
        }
        if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {
            onUpdate(parameter);
        }
        return invocation.proceed();
    }

    private void onInsert(final Object parameter) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        setCreatedAt(parameter, now);
        setUpdatedAt(parameter, now);
    }

    private void onUpdate(final Object parameter) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        setUpdatedAt(parameter, now);
    }

    private void setCreatedAt(final Object target, final Instant value) {
        try {
            final Method setCreatedAt = target.getClass().getDeclaredMethod("setCreatedAt", Instant.class);
            setCreatedAt.invoke(target, value);
        } catch (NoSuchMethodException ignored) {
            // NOOP
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Could not set createdAt field", e);
        }
    }

    private void setUpdatedAt(final Object target, final Instant value) {
        try {
            final Method setUpdatedAt = target.getClass().getDeclaredMethod("setUpdatedAt", Instant.class);
            setUpdatedAt.invoke(target, value);
        } catch (NoSuchMethodException ignored) {
            // NOOP
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Could not set updatedAt field", e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
