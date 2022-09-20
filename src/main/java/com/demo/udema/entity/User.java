package com.demo.udema.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "data")
    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    @Column(name = "name", length = 64)
    private String name;
    @Column(name = "surname", length = 64)
    private String surname;
    @Column(name = "username", unique = true, length = 64)
    private String username;
    @Column(name = "password", length = 128)
    private String password;
    @Column(name = "email", unique = true, length = 128)
    private String email;
    @Column(name = "role", length = 16)
    private String role;
//    @Column(name = "isEnabled")
    private boolean isEnabled;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)// PK
    private List<Course> courses2;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL) // PK
    private List<CourseReviews> courseReviews;


    //-----------ManyToMany---users-orders-courses--------------------
    @ManyToMany (cascade = {CascadeType.ALL})
    @JoinTable(name = "orders",                                         // Table
            joinColumns = {@JoinColumn(name = "user_id")},              // PK FK
            inverseJoinColumns = {@JoinColumn(name = "course_id")})     // PK FK
            Set<Course> courses = new HashSet<>();
    //----------------------------------------------------------------

    @Transient
    private String passwordConfirm;
    @Transient
    private String oldPassword;
    @Transient
    private String newPassword;
    @Transient
    private String passwordNewConfirm;
    @Transient
    private String oldEmail;
    @Transient
    private String newEmail;
    @Transient
    private String emailConfirm;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User(int id, String timeStamp, String name, String surname, String username, String password, String email, String role, boolean isEnabled, List<Course> courses2, List<CourseReviews> courseReviews, Set<Course> courses) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isEnabled = isEnabled;
        this.courses2 = courses2;
        this.courseReviews = courseReviews;
        this.courses = courses;
    }

    public User(String timeStamp, String name, String surname, String username, String password, String email, String role, boolean isEnabled, List<Course> courses2, List<CourseReviews> courseReviews, Set<Course> courses) {
        this.timeStamp = timeStamp;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isEnabled = isEnabled;
        this.courses2 = courses2;
        this.courseReviews = courseReviews;
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<Course> getCourses2() {
        return courses2;
    }

    public void setCourses2(List<Course> courses2) {
        this.courses2 = courses2;
    }

    public List<CourseReviews> getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(List<CourseReviews> courseReviews) {
        this.courseReviews = courseReviews;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordNewConfirm() {
        return passwordNewConfirm;
    }

    public void setPasswordNewConfirm(String passwordNewConfirm) {
        this.passwordNewConfirm = passwordNewConfirm;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getEmailConfirm() {
        return emailConfirm;
    }

    public void setEmailConfirm(String emailConfirm) {
        this.emailConfirm = emailConfirm;
    }
}
