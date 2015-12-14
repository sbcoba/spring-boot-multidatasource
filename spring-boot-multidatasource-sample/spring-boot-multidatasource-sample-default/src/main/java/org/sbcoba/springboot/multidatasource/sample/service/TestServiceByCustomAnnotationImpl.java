package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.sample.annotation.Db1DataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.Db4DataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.Db3DataSource;
import org.sbcoba.springboot.multidatasource.sample.annotation.Db2DataSource;
import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 커스터 어노테이션으로 @DataSource 랩핑한 형태로 가능
 *
 * @see Db1DataSource
 * @see Db2DataSource
 * @see Db3DataSource
 * @see Db4DataSource
 * @author sbcoba
 *
 */
@Service
@Qualifier("custom")
public class TestServiceByCustomAnnotationImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    @Db1DataSource
    @Transactional(readOnly = true)
    public List<String> db1Names() {
        return testDao.getNames();
    }

    @Override
    @Db2DataSource
    @Transactional(readOnly = true)
    public List<String> db2Names() {
        return testDao.getNames();
    }

    @Override
    @Db3DataSource
    @Transactional(readOnly = true)
    public List<String> db3Names() {
        return testDao.getNames();
    }

    @Override
    @Db4DataSource
    @Transactional(readOnly = true)
    public List<String> db4Names() {
        return testDao.getNames();
    }
}
