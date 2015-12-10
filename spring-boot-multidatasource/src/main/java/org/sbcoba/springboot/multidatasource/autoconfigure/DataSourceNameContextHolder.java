package org.sbcoba.springboot.multidatasource.autoconfigure;

import org.springframework.util.Assert;

/**
 * ThreadLocal에 DataSource명을 저장
 *
 * @author sbcoba
 */
public class DataSourceNameContextHolder {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDataSourceName(String dataSourceName) {
		Assert.hasText(dataSourceName, "DataSource name must has text");
		contextHolder.set(dataSourceName);
	}

	public static String getDataSourceName() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}
}
