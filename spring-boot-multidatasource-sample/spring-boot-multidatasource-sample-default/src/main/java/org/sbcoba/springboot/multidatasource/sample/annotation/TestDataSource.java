package org.sbcoba.springboot.multidatasource.sample.annotation;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;

import java.lang.annotation.*;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.DB2;
/**
 * 테스트용 DataSource
 *
 * @author sbcoba
 */
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataSource(DB2)
public @interface TestDataSource {
}
