package com.eric.fall.helloboot.service;

import com.eric.fall.annotation.Autowired;
import com.eric.fall.annotation.Component;
import com.eric.fall.annotation.Transactional;
import com.eric.fall.helloboot.User;
import com.eric.fall.jdbc.JdbcTemplate;

import java.util.List;

@Component
@Transactional
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void initDb() {
        String sql = """
                Create table if not exists f_users (
                email Varchar(50) Primary key, 
                name varchar(255) NOT NULL, 
                password varchar(50) NOT NULL
                );
                """;

        jdbcTemplate.update(sql);

    }

    public User getUser(String email) {
        return jdbcTemplate.queryForObject("select * from f_users where email = ?", User.class, email);
    }

    public List<User> getUsers() {
        return jdbcTemplate.queryForList("select email, name from f_users", User.class);
    }

    public User createUser(String email, String name, String password) {
        User user = new User();
        user.email = email.strip().toLowerCase();
        user.name = name.strip();
        user.password = password;
        jdbcTemplate.update("insert into f_users (email, name, password) values (?, ?, ?)", user.email, user.name, user.password);
        return user;
    }


}
