package com.demo.udema.service;

import com.demo.udema.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public interface UserService {
    void save(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    void saveNoPassword(User user);
    List<String> findUsersWhoBoughtCourseByCourseTitle(String title);
    String findRoleByUsername(String username);
    Integer findIdByUsername(String username);

}
