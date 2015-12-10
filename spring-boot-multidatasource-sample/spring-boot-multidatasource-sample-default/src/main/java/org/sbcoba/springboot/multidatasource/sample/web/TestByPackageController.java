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

	@RequestMapping("dev")
	public List<String> dev() {
		return testService.devNames();
	}

	@RequestMapping("test")
	public List<String> test() {
		return testService.testNames();
	}

	@RequestMapping("staging")
	public List<String> staging() {
		return testService.stagingNames();
	}

	@RequestMapping("prod")
	public List<String> prod() {
		return testService.prodNames();
	}

}
