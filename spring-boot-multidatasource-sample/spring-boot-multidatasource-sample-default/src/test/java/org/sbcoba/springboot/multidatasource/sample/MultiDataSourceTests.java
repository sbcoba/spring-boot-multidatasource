package org.sbcoba.springboot.multidatasource.sample;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sbcoba.springboot.multidatasource.sample.web.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.runners.Parameterized.*;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(Parameterized.class)
@SpringApplicationConfiguration(classes = SampleApplication.class)
@WebAppConfiguration
public class MultiDataSourceTests {
	private static final Logger log = LoggerFactory.getLogger(MultiDataSourceTests.class);

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired 
    WebApplicationContext wac;
    
    MockMvc mockMvc;

    @Parameter(0)
    public String testType;

    @Parameter(1)
    public String methodName;

    @Parameter(2)
    public String dataSourceName;

    @Parameters(name = "testType [{0}], methodName [{1}], dataSourceName [{2}]")
    public static String[][] datasourceData() {
        return new String[][] {
                { "default", "dev", "dev" },
                { "default", "test", "dev" },
                { "default", "staging", "dev" },
                { "default", "prod", "dev" },

                { "method", "dev", "dev" },
                { "method", "test", "test" },
                { "method", "staging", "staging" },
                { "method", "prod", "prod" },

                { "custom", "dev", "dev" },
                { "custom", "test", "test" },
                { "custom", "staging", "staging" },
                { "custom", "prod", "prod" },

                { "class", "dev", "test" },
                { "class", "test", "test" },
                { "class", "staging", "test" },
                { "class", "prod", "test" },

                { "package", "dev", "prod" },
                { "package", "test", "prod" },
                { "package", "staging", "prod" },
                { "package", "prod", "prod" }
        };
    }

    private static Map<String, Class<?>> controllerMap = new HashMap<String, Class<?>>() {{
        put("default", TestController.class);
        put("method", TestByMethodController.class);
        put("custom", TestByCustomAnnotationController.class);
        put("class", TestByClassController.class);
        put("package", TestByPackageController.class);
    }};

        
    @Before 
    public void init() {    
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)   
                	//.alwaysDo(print())
                    .alwaysExpect(status().is2xxSuccessful())
                .build();
    }

    @Test
    public void defaultDataSourceTest() throws Exception {
        log.debug("{} {} {}", testType, methodName, dataSourceName);
        mockMvc.perform(get("/" + testType + "/" + methodName).accept(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(controllerMap.get(testType)))
                .andExpect(handler().methodName(methodName))
                .andExpect(jsonPath("$[0]").value(startsWith(dataSourceName + " dataSource")));
    }

}
