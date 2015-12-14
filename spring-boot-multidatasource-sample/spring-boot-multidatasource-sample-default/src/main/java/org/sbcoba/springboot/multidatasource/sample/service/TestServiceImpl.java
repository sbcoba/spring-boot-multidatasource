package org.sbcoba.springboot.multidatasource.sample.service;

import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 디폴트 @DataSource 를 사용한다.
 * application.xml "spring.multi-datasource.default-datasource-name:" 속성의 영향
 *
 * @see org.sbcoba.springboot.multidatasource.sample.service
 * @author sbcoba
 */
@Primary
@Service
public class TestServiceImpl implements TestService {

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
