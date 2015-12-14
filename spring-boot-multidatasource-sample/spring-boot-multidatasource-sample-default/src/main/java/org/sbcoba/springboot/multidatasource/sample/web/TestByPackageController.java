package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * package-info.java 내부에 있는 @DataSource 를 사용하는 Service를 호출하는 예제
 *
 * @see org.sbcoba.springboot.multidatasource.sample.service.packagetest.TestServiceByPackageImpl
 * @author sbcoba
 */
@RestController
@RequestMapping("package")
public class TestByPackageController {
	@Autowired
	@Qualifier("package")
	private TestService testService;

	@RequestMapping("db1")
	public List<String> db1() {
		return testService.db1Names();
	}

	@RequestMapping("db2")
	public List<String> db2() {
		return testService.db2Names();
	}

	@RequestMapping("db3")
	public List<String> db3() {
		return testService.db3Names();
	}

	@RequestMapping("db4")
	public List<String> db4() {
		return testService.db4Names();
	}

}
