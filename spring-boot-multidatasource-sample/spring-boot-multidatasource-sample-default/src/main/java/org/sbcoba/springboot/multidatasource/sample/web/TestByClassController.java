package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByClassImpl;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByCustomAnnotationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
