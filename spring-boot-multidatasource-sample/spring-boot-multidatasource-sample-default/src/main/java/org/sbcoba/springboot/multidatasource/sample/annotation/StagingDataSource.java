package org.sbcoba.springboot.multidatasource.sample.annotation;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;

import java.lang.annotation.*;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.DB3;
/**
 * 스테이징용 DataSource
 *
 * @author sbcoba
 */
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataSource(DB3)
public @interface StagingDataSource {
}
