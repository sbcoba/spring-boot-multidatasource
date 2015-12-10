package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * DataSource Routing
 *
 * @author sbcoba
 */
public class MultiRoutingDataSource extends AbstractRoutingDataSource {
    private Map<String, DataSourceSet> targetDataSourceSet;

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceNameContextHolder.getDataSourceName();
    }

    public void setTargetDataSourceSet(Map<String, DataSourceSet> targetDataSourceSet) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        for (Map.Entry<String, DataSourceSet> entry : targetDataSourceSet.entrySet()) {
            targetDataSources.put(entry.getKey(), entry.getValue().getDataSource());
        }
        super.setTargetDataSources(targetDataSources);
        this.targetDataSourceSet = targetDataSourceSet;
    }

    public Map<String, DataSourceSet> getTargetDataSourceSet() {
        return targetDataSourceSet;
    }
}
