package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.sample.annotation.DevDataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.ProdDataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.StagingDataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.TestDataSource;
import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 커스터 어노테이션으로 @DataSource 랩핑한 형태로 가능
 *
 * @see DevDataSource
 * @see TestDataSource
 * @see StagingDataSource
 * @see ProdDataSource
 * @author sbcoba
 *
 */
@Service
@Qualifier("custom")
public class TestServiceByCustomAnnotationImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    @DevDataSource
    @Transactional(readOnly = true)
    public List<String> db1Names() {
        return testDao.getNames();
    }

    @Override
    @TestDataSource
    @Transactional(readOnly = true)
    public List<String> db2Names() {
        return testDao.getNames();
    }

    @Override
    @StagingDataSource
    @Transactional(readOnly = true)
    public List<String> db3Names() {
        return testDao.getNames();
    }

    @Override
    @ProdDataSource
    @Transactional(readOnly = true)
    public List<String> db4Names() {
        return testDao.getNames();
    }
}
