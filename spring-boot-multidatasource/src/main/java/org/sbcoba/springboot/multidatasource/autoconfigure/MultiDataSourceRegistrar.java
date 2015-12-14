package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.springframework.beans.factory.BeanCreationException;
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
 * @author sbcoba
 */
public class MultiDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MultiDataSourceProperties multiDataSourceProperties = getMultiDataSourceProperties();
        Map<String, DataSourceProperties> dataSourceProperties = multiDataSourceProperties.getDatasources();
        for (Map.Entry<String, DataSourceProperties> entry : dataSourceProperties.entrySet()) {
            String datasourceName = entry.getKey();
            DataSourceProperties properties = entry.getValue();
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(DataSourceFactory.class)
                    .addPropertyValue("classLoader", properties.getClassLoader())
                    .addPropertyValue("driverClassName", properties.getDriverClassName())
                    .addPropertyValue("url", properties.getUrl())
                    .addPropertyValue("username", properties.getUsername())
                    .addPropertyValue("password", properties.getPassword());
            if (properties.getType() != null) {
                bdb.addPropertyValue("type", properties.getType());
            }
            registry.registerBeanDefinition(datasourceName, bdb.getBeanDefinition());
        }
    }

    private MultiDataSourceProperties getMultiDataSourceProperties() {
        MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
        PropertiesConfigurationFactory<MultiDataSourceProperties> factory =
                new PropertiesConfigurationFactory<MultiDataSourceProperties>(multiDataSourceProperties);
        factory.setConversionService(new DefaultConversionService());
        ConfigurationProperties annotation =
                AnnotationUtils.findAnnotation(MultiDataSourceProperties.class, ConfigurationProperties.class);
        String prefix = StringUtils.hasText(annotation.value()) ? annotation.value() : annotation.prefix();
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
                    + MultiDataSourceProperties.class + " (" + annotation + ")", e);
        }
        return multiDataSourceProperties;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
