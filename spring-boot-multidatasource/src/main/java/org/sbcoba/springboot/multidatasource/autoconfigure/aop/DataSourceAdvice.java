package org.sbcoba.springboot.multidatasource.autoconfigure.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.autoconfigure.DataSourceNameContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * DataSource advice
 *
 * @author sbcoba
 */
public class DataSourceAdvice implements MethodInterceptor, Ordered {
    private static final Logger log = LoggerFactory.getLogger(DataSourceAdvice.class);
    private int order = 0;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method invocationMethod = invocation.getMethod();
        log.debug("Methods \"{}\" annotated with @Datasource is start", invocationMethod.toString());
        DataSource dataSource = findDataSourceAnnotation(invocationMethod, invocation.getThis().getClass());
        if (dataSource != null) {
            String currentDataSourceName = DataSourceNameContextHolder.getDataSourceName();
            String currentSettingDataSourceName = dataSource.value();
            if (currentSettingDataSourceName != null &&
                    !currentSettingDataSourceName.equals(currentDataSourceName)) {
                DataSourceNameContextHolder.setDataSourceName(currentSettingDataSourceName);
                log.debug("'{}' changed dataSource", currentSettingDataSourceName);
            }
        }
        try {
            return invocation.proceed();
        } finally {
            DataSourceNameContextHolder.clear();
            log.debug("Methods \"{}\" annotated with @Datasource is end", invocationMethod.toString());
        }
    }

    /**
     * Find @DataSource
     * Order: Method -> Class -> package
     *
     * @param method
     * @return
     * @throws Throwable
     */
    private DataSource findDataSourceAnnotation(Method method, Class<?> targetClass) throws Throwable {
        DataSource dataSource;
        if ((dataSource = AnnotationUtils.findAnnotation(method, DataSource.class)) != null) {
            log.debug("Method {}.{} has @DataSource(value={})", targetClass.getSimpleName(), method.getName(), dataSource.value());
            return dataSource;
        }

        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        if ((dataSource = AnnotationUtils.findAnnotation(specificMethod, DataSource.class)) != null) {
            log.debug("Specific Method {}.{} has @DataSource(value={})", targetClass.getSimpleName(), method.getName(), dataSource.value());
            return dataSource;
        }

        if ((dataSource = AnnotationUtils.findAnnotation(targetClass, DataSource.class)) != null) {
            log.debug("Class {} has @DataSource(value={})", targetClass.getSimpleName(), dataSource.value());
            return dataSource;
        }
        if ((dataSource = AnnotationUtils.findAnnotation(targetClass.getPackage(), DataSource.class)) != null) {
            log.debug("Package {} has @DataSource(value={})", targetClass.getPackage().toString(), dataSource.value());
            return dataSource;
        }
        return null;
    }


    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
