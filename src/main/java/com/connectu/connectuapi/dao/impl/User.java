package com.connectu.connectuapi.dao.impl;

import com.github.javafaker.Faker;
import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String 	email;
    private String password;
    private String userName;
    public static User createFakeUser() {
        Faker faker = new Faker();
        User user = new User();
        String username = faker.internet().password();
        user.setEmail(faker.internet().emailAddress(username));
        user.setPassword(faker.internet().password());
        user.setUserName(faker.name().name());
        return user;
    }
}
