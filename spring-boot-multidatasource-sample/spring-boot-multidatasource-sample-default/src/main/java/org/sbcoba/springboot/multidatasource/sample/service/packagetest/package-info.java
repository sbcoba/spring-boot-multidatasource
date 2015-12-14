/**
 * 해당 페키지 안의 모든 클래스에 동일한 DataSource를 적용
 * ( 범위가 넓기 때문에 사용시 주의 필요 )
 *
 * @author sbcoba
 */
@DataSource(DB4)
package org.sbcoba.springboot.multidatasource.sample.service.packagetest;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.*;