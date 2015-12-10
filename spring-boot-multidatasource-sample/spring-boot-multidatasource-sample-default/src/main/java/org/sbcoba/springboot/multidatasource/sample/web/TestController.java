package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByClassImpl;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DataSource 어노테이션을 설정하지 않았을때 Default DataSource를 사용하는 예제
 *
 * @see TestServiceImpl
 * @author sbcoba
 */
@RestController
@RequestMapping("default")
public class TestController {
	@Autowired
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
