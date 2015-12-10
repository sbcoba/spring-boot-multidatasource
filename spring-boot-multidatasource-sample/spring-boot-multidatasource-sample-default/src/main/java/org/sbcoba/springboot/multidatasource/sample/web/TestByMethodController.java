package org.sbcoba.springboot.multidatasource.sample.web;

import org.sbcoba.springboot.multidatasource.autoconfigure.DataSource;
import org.sbcoba.springboot.multidatasource.sample.service.TestService;
import org.sbcoba.springboot.multidatasource.sample.service.TestServiceByMethodmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 메소드별 DataSource를 따로 사용한 서비스를 호출
 *
 * @see TestServiceByMethodmpl
 * @author sbcoba
 */
@RestController
@RequestMapping("method")
public class TestByMethodController {

	@Autowired
	@Qualifier("method")
	private TestService testService;

	@RequestMapping("dev")
	public List<String> dev() {
		return testService.devNames();
	}

	@RequestMapping("test")
	@DataSource("prod")
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
