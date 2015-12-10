package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataSource 각각 초기화
 * @author sbcoba
 */
class MultiDataSourceInitializer implements ApplicationListener<MultiDataSourceInitializedEvent> {
    private final Logger log = LoggerFactory.getLogger(MultiDataSourceInitializer.class);

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    private MultiRoutingDataSource multiRoutingDataSource;
    private Map<String, Boolean> dataSourceInitializedMap = new ConcurrentHashMap<String, Boolean>();

    @PostConstruct
    public void init() {
        if (this.applicationContext.getBeanNamesForType(AbstractRoutingDataSource.class, false,
                false).length > 0) {
            this.multiRoutingDataSource = this.applicationContext.getBean(MultiRoutingDataSource.class);
        }
        if (this.multiRoutingDataSource == null) {
            log.debug("No DataSource found so not initializing");
            return;
        }
        runSchemaScripts();
    }

    private void runSchemaScripts() {
        Map<String, DataSourceSet> targetDataSourceSet = multiRoutingDataSource.getTargetDataSourceSet();
        for (Map.Entry<String, DataSourceSet> entry : targetDataSourceSet.entrySet()) {
            String dataSourceName = entry.getKey();
            DataSourceSet dataSourceSet = entry.getValue();
            DataSource dataSource = dataSourceSet.getDataSource();
            DataSourceProperties dataSourceProperties = dataSourceSet.getDataSourceProperties();
            if (!dataSourceProperties.isInitialize()) {
                log.debug("Initialization disabled (not running DDL scripts)");
                continue;
            }
            List<Resource> scripts = getScripts(dataSourceProperties.getSchema(), "schema", dataSourceProperties);
            if (!scripts.isEmpty()) {
                runScripts(scripts, dataSource, dataSourceProperties);
                try {
                    this.applicationContext.publishEvent(
                            new MultiDataSourceInitializedEvent(dataSource, dataSourceName, dataSourceProperties));
                    // The listener might not be registered yet, so don't rely on it.
                    Boolean initialized = dataSourceInitializedMap.get(dataSourceName);
                    if (initialized == null || initialized == false) {
                        runDataScripts(dataSource, dataSourceProperties);
                        dataSourceInitializedMap.put(dataSourceName, true);
                    }

                } catch (IllegalStateException ex) {
                    log.warn("Could not send event to complete DataSource initialization ("
                            + ex.getMessage() + ")");
                }
            }
        }
    }

    @Override
    public void onApplicationEvent(MultiDataSourceInitializedEvent event) {
        DataSourceProperties dataSourceProperties = event.getDataSourceProperties();
        if (!dataSourceProperties.isInitialize()) {
            log.debug("Initialization disabled (not running data scripts)");
            return;
        }
        String dataSourceName = event.getDataSourceName();
        Boolean initialized = dataSourceInitializedMap.get(dataSourceName);
        if (initialized == null || initialized == false) {
            runDataScripts((javax.sql.DataSource) event.getSource(), dataSourceProperties);
            dataSourceInitializedMap.put(dataSourceName, true);
        }
    }

    private void runDataScripts(javax.sql.DataSource dataSource,
                                DataSourceProperties dataSourceProperties) {
        List<Resource> scripts = getScripts(dataSourceProperties.getData(), "data", dataSourceProperties);
        runScripts(scripts, dataSource, dataSourceProperties);
    }

    private List<Resource> getScripts(String locations, String fallback, DataSourceProperties dataSourceProperties) {
        if (locations == null) {
            String platform = dataSourceProperties.getPlatform();
            locations = "classpath*:" + fallback + "-" + platform + ".sql,";
            locations += "classpath*:" + fallback + ".sql";
        }
        return getResources(locations);
    }

    private List<Resource> getResources(String locations) {
        List<Resource> resources = new ArrayList<Resource>();
        for (String location : StringUtils.commaDelimitedListToStringArray(locations)) {
            try {
                for (Resource resource : this.applicationContext.getResources(location)) {
                    if (resource.exists()) {
                        resources.add(resource);
                    }
                }
            } catch (IOException ex) {
                throw new IllegalStateException(
                        "Unable to load resource from " + location, ex);
            }
        }
        return resources;
    }

    private void runScripts(List<Resource> resources, DataSource dataSource,
                            DataSourceProperties dataSourceProperties) {
        if (resources.isEmpty()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(dataSourceProperties.isContinueOnError());
        populator.setSeparator(dataSourceProperties.getSeparator());
        if (dataSourceProperties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(dataSourceProperties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

}
