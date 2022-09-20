package com.demo.udema.repositoryDAO;

import com.demo.udema.entity.CourseReviews;
import com.demo.udema.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(int id);
    User findByEmail(String email);
    @Query(value = "SELECT users.username FROM Users" +
            " WHERE users.id IN" +
            "(SELECT orders.user_id FROM orders" +
            " WHERE orders.course_id IN" +
            "(SELECT courses.id FROM courses" +
            " WHERE courses.title LIKE ?1))", nativeQuery = true)
    List<String> findUsersWhoBoughtCourseByCourseTitle(String title);

    @Query(value = "SELECT role FROM users" +
            " WHERE username LIKE ?1", nativeQuery = true)
    String findRoleByUsername(String username);

    @Query(value = "SELECT id FROM users" +
            " WHERE username LIKE ?1", nativeQuery = true)
    Integer findIdByUsername(String username);
}
