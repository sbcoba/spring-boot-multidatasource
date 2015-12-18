package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import java.util.Map;

/**
 * properties로 부터 가져온 Multi DataSource DB 정보를 기초로
 * DataSource 객체생성 후 Spring 컨테이너로 등록
 *
 * @author sbcoba
 */
public class MultiDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ConfigurationProperties annotation =
                AnnotationUtils.findAnnotation(MultiDataSourceProperties.class, ConfigurationProperties.class);
        String prefix = StringUtils.hasText(annotation.value()) ? annotation.value() : annotation.prefix();

        MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
        MultiDataSourceRegistrar.bindProperties(multiDataSourceProperties, prefix, environment);

        Map<String, DataSourceProperties> dataSourceProperties = multiDataSourceProperties.getDatasources();
        for (Map.Entry<String, DataSourceProperties> entry : dataSourceProperties.entrySet()) {
            String datasourceName = entry.getKey();
            DataSourceProperties properties = entry.getValue();
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(DataSourceFactory.class)
                    .addPropertyValue("classLoader", properties.getClassLoader())
                    .addPropertyValue("driverClassName", properties.getDriverClassName())
                    .addPropertyValue("url", properties.getUrl())
                    .addPropertyValue("username", properties.getUsername())
                    .addPropertyValue("password", properties.getPassword())
                    .addPropertyValue("environment", environment)
                    .addPropertyValue("environmentPrefix", prefix + ".datasources." + datasourceName);
            if (properties.getType() != null) {
                bdb.addPropertyValue("type", properties.getType());
            }
            AbstractBeanDefinition beanDefinition = bdb.getBeanDefinition();
            beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, datasourceName));
            registry.registerBeanDefinition(datasourceName, beanDefinition);
        }
    }

    /**
     * 객체와 environment 정보를 Binding
     *
     * @param t Binding 대상 객체
     * @param prefix environment 의 prefix
     * @param environment Spring 에서 관리하는 환경정보 Bean
     * @param <T> Binding 대상 타입
     * @return Binding 완료된 객체
     */
    static <T> T bindProperties(T t, String prefix, Environment environment) {
        PropertiesConfigurationFactory<T> factory = new PropertiesConfigurationFactory<T>(t);
        factory.setConversionService(new DefaultConversionService());
        factory.setTargetName(prefix);

        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
            MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            factory.setPropertySources(propertySources);
        }

        try {
            factory.bindPropertiesToTarget();
        } catch (BindException e) {
            throw new BeanCreationException("MultiDataSourceProperties", "Could not bind properties to "
                    + t.getClass() + " (" + prefix + ")", e);
        }
        return t;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
