package org.sbcoba.springboot.multidatasource.autoconfigure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.autoconfigure.DataSourceNameContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 사용하지 않는 클래스
 */
@Deprecated
//@Aspect
public class DataSourceRouterAspect implements Ordered {
    private final Logger log = LoggerFactory.getLogger(DataSourceRouterAspect.class);
    private int order = 0;
    /**
     * 데이타소스 어노테이션 클래스를 처리하는 메소드를 나타내는 Pointcut
     */
    @Pointcut( "@within(org.sbcoba.springboot.multidatasource.autoconfigure.DataSource)" )
    public void classDataSourceAnno() {}
    /**
     * 데이타소스 어노테이션 메소드를 나타내는 Pointcut
     */
    @Pointcut( "@annotation(org.sbcoba.springboot.multidatasource.autoconfigure.DataSource)" )
    public void methodDataSourceAnno() {}

    /**
     * 데이타소스 처리 메소드를 나타내는 Pointcut
     */
    @Pointcut( "classDataSourceAnno() || methodDataSourceAnno()" )
    public void dataSource() {}

    @Around("dataSource()")
    public Object invoke(ProceedingJoinPoint point) throws Throwable {
        log.debug("Methods \"{}\" @Datasource is start", point.toLongString());
        DataSource datasource = findDataSourceAnnotation(point);
        if (datasource != null) {
            String currentDataSourceName = DataSourceNameContextHolder.getDataSourceName();
            String currentSettingDataSourceName = datasource.value();
            if (currentSettingDataSourceName != null &&
                    !currentSettingDataSourceName.equals(currentDataSourceName)) {
                DataSourceNameContextHolder.setDataSourceName(currentSettingDataSourceName);
                log.debug("'{}' change datasource", currentSettingDataSourceName);
            }
        }
        try {
            return point.proceed();
        } finally {
            DataSourceNameContextHolder.clear();
            log.debug("Methods \"{}\" annotated with @Datasource is start", point.toLongString());
        }
    }

    /**
     * Find DataSource annotation
     *
     * @param point
     * @return
     * @throws Throwable
     */
    private DataSource findDataSourceAnnotation(ProceedingJoinPoint point) throws Throwable {
        Method method = findExecutedMethod(point);
        DataSource dataSource = AnnotationUtils.findAnnotation(method, DataSource.class);
        if (dataSource == null) {
            dataSource = AnnotationUtils.findAnnotation(
                    point.getTarget().getClass(), DataSource.class);
        }

        if (dataSource == null) {
            dataSource = AnnotationUtils.findAnnotation(
                    point.getTarget().getClass().getPackage(), DataSource.class);
        }
        return dataSource;
    }

    /**
     * Find executed method
     *
     * @param point
     * @return
     * @throws NoSuchMethodException
     */
    private Method findExecutedMethod(ProceedingJoinPoint point)
            throws NoSuchMethodException {
        final MethodSignature methodSignature =
                (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            String methodName = methodSignature.getName();
            method = point.getTarget().getClass()
                    .getDeclaredMethod(methodName, method.getParameterTypes());
        }
        return method;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static void main(String[] args) {
        DataSource annotation = AnnotationUtils.findAnnotation(DataSourceRouterAspect.class.getPackage(), DataSource.class);
        System.out.println(annotation);
    }
}
