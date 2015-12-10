package org.sbcoba.springboot.multidatasource.sample;

/**
 * DataSource 명  리스트
 * ( 문자열로 바로 사용할 수 있으나
 * 오타 등의 문제로 인해 상수로 사용하는 것을 추천 )
 *
 * @author sbcoba
 */
public final class DataSourceName {
    /**
     * 개발 DataSource 명
     */
    public static final String DEV = "dev";
    /**
     * 테스트 DataSource 명
     */
    public static final String TEST ="test";
    /**
     * 스테이징 DataSource 명
     */
    public static final String STAGING = "staging";
    /**
     * 운영 DataSource 명
     */
    public static final String PROD = "prod";
}
