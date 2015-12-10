package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.*;

/**
 * 메소드별 개별의 DataSource를 따로 사용한 서비스
 *
 * @author sbcoba
 */
@Service
@Qualifier("method")
public class TestServiceByMethodmpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    @DataSource(DEV)
    @Transactional(readOnly = true)
    public List<String> devNames() {
        return testDao.getNames();
    }

    @Override
    @DataSource(TEST)
    @Transactional(readOnly = true)
    public List<String> testNames() {
        return testDao.getNames();
    }

    @Override
    @DataSource(STAGING)
    @Transactional(readOnly = true)
    public List<String> stagingNames() {
        return testDao.getNames();
    }

    @Override
    @DataSource(PROD)
    @Transactional(readOnly = true)
    public List<String> prodNames() {
        return testDao.getNames();
    }
}
