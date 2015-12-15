package org.sbcoba.springboot.multidatasource.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;
import static org.sbcoba.springboot.multidatasource.sample.DataSourceName.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleApplication.class)
public class DataSourceTests {
    //private static final Logger log = LoggerFactory.getLogger(DataSourceTests.class);
    private static final String TEST_QUERY = "SELECT name FROM TEST";

    @Autowired @Qualifier(DB1)
    private DataSource db1DataSource;

    @Autowired @Qualifier(DB2)
    private DataSource db2DataSource;

    @Autowired @Qualifier(DB3)
    private DataSource db3DataSource;

    @Autowired @Qualifier(DB4)
    private DataSource db4DataSource;

    @Test
    public void db1DataSourceTest() {
        List<String> result = getTests(db1DataSource);
        System.out.println(DB1 + " result " + result);
        assertThat(result.get(0), startsWith(DB1));
    }

    @Test
    public void db2DataSourceTest() {
        List<String> result = getTests(db2DataSource);
        System.out.println(DB2 + " result " + result);
        assertThat(result.get(0), startsWith(DB2));
    }

    @Test
    public void db3DataSourceTest() {
        List<String> result = getTests(db3DataSource);
        System.out.println(DB3 + " result " + result);
        assertThat(result.get(0), startsWith(DB3));
    }

    @Test
    public void db4DataSourceTest() {
        List<String> result = getTests(db4DataSource);
        System.out.println(DB4 + " result " + result);
        assertThat(result.get(0), startsWith(DB4));
    }

    private List<String> getTests(DataSource dataSource) {
        return new JdbcTemplate(dataSource)
                .queryForList(TEST_QUERY, String.class);
    }
}