package com.demo.udema.security;

import com.demo.udema.entity.User;

import javax.persistence.*;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private  String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER )
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
