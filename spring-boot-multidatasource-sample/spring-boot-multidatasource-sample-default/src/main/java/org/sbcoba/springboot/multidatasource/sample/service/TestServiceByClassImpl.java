package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.DEV;
import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.TEST;

/**
 * Class 위에 @DataSource 어노테이션 사용으로
 * 모든 메소드에서 해당 DataSource를 사용
 *
 * @author sbcoba
 */
@Service
@DataSource(TEST)
@Qualifier("class")
public class TestServiceByClassImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    @Transactional(readOnly = true)
    public List<String> devNames() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> testNames() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> stagingNames() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> prodNames() {
        return testDao.getNames();
    }
}
