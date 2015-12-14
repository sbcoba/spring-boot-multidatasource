package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByClassImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Class 위에 @DataSource 어노테이션 사용한 서비스를 호출
 *
 * @see TestServiceByClassImpl
 * @author sbcoba
 */
@RestController
@RequestMapping("class")
public class TestByClassController {
	@Autowired
	@Qualifier("class")
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
