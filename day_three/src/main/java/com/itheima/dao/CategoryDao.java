package com.itheima.dao;

import com.itheima.model.Category;
import com.itheima.util.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDao {
    public CategoryDao() {
    }

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());


    public List<Category> findAllCategory() throws Exception {
            String sql = "SELECT * FROM tab_category ORDER BY cid;";
            return  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }
}
