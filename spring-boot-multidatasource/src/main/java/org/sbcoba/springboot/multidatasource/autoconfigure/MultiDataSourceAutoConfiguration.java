package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.aopalliance.intercept.MethodInterceptor;
import org.sbcoba.springboot.multidatasource.autoconfigure.aop.AnnotationDeepMethodMatcher;
import org.sbcoba.springboot.multidatasource.autoconfigure.aop.DataSourceAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Boot AutoConfiguration 설정값
 *
 * @author sbcoba
 */
@EnableConfigurationProperties(MultiDataSourceProperties.class)
@ConditionalOnMissingBean(MultiDataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.multiDatasource", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@Configuration
public class MultiDataSourceAutoConfiguration {
	private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAutoConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MultiDataSourceProperties multiDataSourceProperties;

    @Bean
    @Primary
    @ConditionalOnMissingBean(AbstractRoutingDataSource.class)
    public javax.sql.DataSource routeDataSource() {
        MultiRoutingDataSource routingDataSource = new MultiRoutingDataSource();

        Map<String, DataSourceSet> targetDataSourceSet = new HashMap<String, DataSourceSet>();
        Map<String, DataSourceProperties> dataSourceProperties = multiDataSourceProperties.getDatasources();
        for (Map.Entry<String, DataSourceProperties> entry : dataSourceProperties.entrySet()) {
            String datasourceName = entry.getKey();
            DataSourceProperties properties = entry.getValue();
            DataSourceBuilder factory = DataSourceBuilder
                    .create(properties.getClassLoader())
                    .driverClassName(properties.getDriverClassName())
                    .url(properties.getUrl()).username(properties.getUsername())
                    .password(properties.getPassword());
            if (properties.getType() != null) {
                factory.type(properties.getType());
            }
            javax.sql.DataSource dataSource = factory.build();
            targetDataSourceSet.put(datasourceName, new DataSourceSet(dataSource, properties));
        }

        if (targetDataSourceSet.size() == 0) {
            Map<String,  javax.sql.DataSource> dataSourceMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,  javax.sql.DataSource.class);
            for (Map.Entry<String,  javax.sql.DataSource> entry : dataSourceMap.entrySet()) {
                targetDataSourceSet.put(entry.getKey(), new DataSourceSet(entry.getValue()));
            }
        }
        Assert.isTrue(targetDataSourceSet.size() != 0, "Datasource not found");

        routingDataSource.setTargetDataSourceSet(targetDataSourceSet);
        routingDataSource.setDefaultTargetDataSource(getDefaultTargetDatasource(targetDataSourceSet));
        return routingDataSource;
    }

    private Object getDefaultTargetDatasource(Map<String, DataSourceSet> targetDataSourceSet) {
        String datasourceName = multiDataSourceProperties.getDefaultDatasourceName();
        if (!StringUtils.hasText(datasourceName)) {
            datasourceName = targetDataSourceSet.keySet().toArray()[0].toString();
        }
        DataSourceSet dataSourceSet = targetDataSourceSet.get(datasourceName);
        if (dataSourceSet == null) {
            return null;
        }
        return dataSourceSet.getDataSource();
    }

    @Bean
    @DependsOn("routeDataSource")
    public MultiDataSourceInitializer multiDataSourceInitializer() {
        return new MultiDataSourceInitializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodInterceptor dataSourceAdvice() {
        DataSourceAdvice dataSourceAdvice = new DataSourceAdvice();
        dataSourceAdvice.setOrder(Ordered.LOWEST_PRECEDENCE);
        return dataSourceAdvice;
    }

    @Bean
    @ConditionalOnMissingBean
    public PointcutAdvisor potincutAdviser(MethodInterceptor dataSourceAdvice) {
        DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();
        advisor.setPointcut(dataSourcePointcut());
        advisor.setAdvice(dataSourceAdvice);
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public Pointcut dataSourcePointcut() {
        List<String> basePackages = multiDataSourceProperties.getBasePackages();
        if (basePackages == null || basePackages.size() == 0) {
            basePackages = AutoConfigurationPackages.get(applicationContext);
        }
        return new DataSourcePointcut(basePackages);
    }

    static class DataSourcePointcut implements Pointcut {
        private List<String> packages = new ArrayList<String>();

        public DataSourcePointcut(List<String> packages) {
            this.packages = packages;
        }

        @Override
        public ClassFilter getClassFilter() {
            return new ClassFilter() {
                @Override
                public boolean matches(Class<?> clazz) {
                    for (String packageName : packages) {
                        log.debug("{} | {} : {}", packageName, clazz.getName(), packageName.startsWith(clazz.getName()));
                        if (clazz.getName().startsWith(packageName)){
                            return true;
                        }
                    }
                    return false;
                }
            };
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new AnnotationDeepMethodMatcher(DataSource.class);
        }

    }
}
