package org.sbcoba.springboot.multidatasource.sample.service;

import java.util.List;

/**
 * 테스트에 사용할 인터페이스
 *
 * @author sbcoba
 */
public interface TestService {
    List<String> db1Names();

    List<String> db2Names();

    List<String> db3Names();

    List<String> db4Names();
}
