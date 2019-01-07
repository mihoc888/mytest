package com.itheima.dao;

import com.itheima.model.User;
import com.itheima.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public UserDao() {
    }

    /**
     * 根据登陆的用户名,查看是否存在用户
     * @param username
     * @return User
     */
    public User getUserByUserName(String username)  {
        try{
            String sql = "SELECT * FROM tab_user WHERE username=?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }


    /**
     * 用户注册
     * @param user
     * @return  int
     */
    public int register(User user) {
        String sql = "INSERT INTO tab_user VALUES (NULL,?,?,?,?,?,?,?);";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(), user.getEmail()
        );
    }


}
