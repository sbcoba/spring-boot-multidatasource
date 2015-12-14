package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
