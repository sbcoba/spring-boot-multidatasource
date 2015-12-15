package org.sbcoba.springboot.multidatasource.autoconfigure;

import java.lang.annotation.*;

/**
 * Choice DataSource annotation
 *
 * @author sbcoba
 *
 */
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    /**
     * @return datasource name
     */
    String value();
}
