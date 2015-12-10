package org.sbcoba.springboot.multidatasource.sample.service.packagetest;

import org.sbcoba.springboot.multidatasource.sample.repository.TestDao;
import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * package-info.java 내부에 있는 @DataSource 를 사용한다.
 *
 * @see org.sbcoba.springboot.multidatasource.sample.service.packagetest
 * @author sbcoba
 */
@Service
@Qualifier("package")
public class TestServiceByPackageImpl implements TestService {

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
