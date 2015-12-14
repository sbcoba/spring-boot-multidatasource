package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.DB2;

/**
 * Class 위에 @DataSource 어노테이션 사용으로
 * 모든 메소드에서 해당 DataSource를 사용
 *
 * @author sbcoba
 */
@Service
@DataSource(DB2)
@Qualifier("class")
public class TestServiceByClassImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    @Transactional(readOnly = true)
    public List<String> db1Names() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> db2Names() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> db3Names() {
        return testDao.getNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> db4Names() {
        return testDao.getNames();
    }
}
