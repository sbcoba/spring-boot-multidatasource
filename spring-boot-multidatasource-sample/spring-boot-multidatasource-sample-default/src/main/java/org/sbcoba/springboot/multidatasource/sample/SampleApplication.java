package org.sbcoba.springboot.multidatasource.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 샘플 실행 진입점
 * @author sbcoba
 */
@SpringBootApplication
public class SampleApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
    	SpringApplication.run(SampleApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SampleApplication.class);
    }
}
