package org.sbcoba.springboot.multidatasource.sample.annotation;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;

import java.lang.annotation.*;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.DEV;
import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.TEST;
/**
 * 테스트용 DataSource
 *
 * @author sbcoba
 */
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataSource(TEST)
public @interface TestDataSource {
}
