package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static org.sbcoba.springboot.multidatasource.autoconfigure.MultiDataSourceRegistrar.*;

/**
 * DataSourceBuilder를 랩핑하여 DateSource를 생성하는 FactoryBean
 *
 * @author sbcoba
 */
public class DataSourceFactory implements FactoryBean<javax.sql.DataSource> {
    
    private Class<? extends DataSource> type;
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private ClassLoader classLoader;

    private Environment environment;
    private String environmentPrefix;


    public DataSourceFactory() {
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setEnvironmentPrefix(String environmentPrefix) {
        this.environmentPrefix = environmentPrefix;
    }

    @Override
    public DataSource getObject() throws Exception {
        DataSourceBuilder builder = DataSourceBuilder
                .create(classLoader)
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password);
        if (type != null) {
            builder.type(type);
        }
        DataSource dataSource = builder.build();
        bindProperties(dataSource, environmentPrefix, environment);
        return dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return javax.sql.DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
