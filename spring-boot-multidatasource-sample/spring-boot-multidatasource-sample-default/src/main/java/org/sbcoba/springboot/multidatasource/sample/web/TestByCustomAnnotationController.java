package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByCustomAnnotationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 커스터 어노테이션으로 @DataSource 랩핑한 형태의 서비스를 호출
 *
 * @see TestServiceByCustomAnnotationImpl
 * @author sbcoba
 */
@RestController
@RequestMapping("custom")
public class TestByCustomAnnotationController {

	@Autowired
	@Qualifier("custom")
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
