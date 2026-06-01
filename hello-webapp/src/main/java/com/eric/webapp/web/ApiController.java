package com.eric.webapp.web;

import com.eric.fall.annotation.Autowired;
import com.eric.fall.annotation.GetMapping;
import com.eric.fall.annotation.PathVariable;
import com.eric.fall.annotation.RestController;
import com.eric.fall.exception.DataAccessException;
import com.eric.webapp.User;
import com.eric.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @GetMapping("/api/user/{email}")
    Map<String, Boolean> userExist(@PathVariable("email") String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        try {
            userService.getUser(email);
            return Map.of("result", Boolean.TRUE);
        } catch (DataAccessException e) {
            return Map.of("result", Boolean.FALSE);
        }
    }

    @GetMapping("/api/users")
    List<User> users() {
        return userService.getUsers();
    }
















}
