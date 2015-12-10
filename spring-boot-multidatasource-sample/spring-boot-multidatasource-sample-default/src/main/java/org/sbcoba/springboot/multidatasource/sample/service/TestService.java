package org.sbcoba.springboot.multidatasource.sample.service;

import java.util.List;

/**
 * 테스트에 사용할 인터페이스
 * @author sbcoba
 */
public interface TestService {
    List<String> devNames();

    List<String> testNames();

    List<String> stagingNames();

    List<String> prodNames();
}
