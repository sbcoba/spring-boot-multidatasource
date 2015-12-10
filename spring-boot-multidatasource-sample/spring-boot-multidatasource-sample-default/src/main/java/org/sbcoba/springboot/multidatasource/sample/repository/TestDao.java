package org.sbcoba.springboot.multidatasource.sample.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 테스트 데이타 조회용 DAO
 *
 * @author sbcoba
 */
@Repository
public class TestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getNames() {
        return jdbcTemplate.queryForList("SELECT name FROM TEST", String.class);
    }
}
