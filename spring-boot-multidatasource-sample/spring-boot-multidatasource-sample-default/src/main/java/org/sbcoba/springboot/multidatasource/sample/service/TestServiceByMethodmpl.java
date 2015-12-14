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
    @DataSource(DB1)
    @Transactional(readOnly = true)
    public List<String> db1Names() {
        return testDao.getNames();
    }

    @Override
    @DataSource(DB2)
    @Transactional(readOnly = true)
    public List<String> db2Names() {
        return testDao.getNames();
    }

    @Override
    @DataSource(DB3)
    @Transactional(readOnly = true)
    public List<String> db3Names() {
        return testDao.getNames();
    }

    @Override
    @DataSource(DB4)
    @Transactional(readOnly = true)
    public List<String> db4Names() {
        return testDao.getNames();
    }
}
