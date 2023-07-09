package com.connectu.connectuapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.*;
import com.connectu.connectuapi.service.IFriendshipService;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IThreadService threadService;
    @Autowired
    private IFriendshipService friendshipService;
    public void addFakeUsers(int count) {
        for (int i = 0; i < count; i++) {
            User fakeUser = UserServiceImpl.createFakeUser();
            userDao.insert(fakeUser);
        }
    }

    @Override
    public User getById(Serializable id) {

        friendshipService.followerNum((Integer) id);
        friendshipService.followingNum((Integer) id);
        return super.getById(id);
    }


    @Override
    public List<User> getSortedUsers() {
        List<User> users = userDao.selectList(null);
        Map<Integer, Integer> userHotScores = new HashMap<>();
        List<Integer> userIds = users.stream().map(User::getUserId).collect(Collectors.toList());
        List<List<Thread>> userThreads = threadService.getUserThreadForUser(userIds);
        for (int i = 0; i < users.size(); i++) {
            int hotScoreSum = 0;
            List<Thread> threads = userThreads.get(i);
            for (Thread thread : threads) {
                hotScoreSum += thread.getHotScore();
            }
            userHotScores.put(users.get(i).getUserId(), hotScoreSum);
        }
        users.sort(Comparator.comparingInt((User user) -> userHotScores.get(user.getUserId())).reversed());
        return users;
    }

    public static User createFakeUser() {
        Faker faker = new Faker(new Locale("zh-CN"));
        User user = new User();
        String username = faker.internet().password();
        user.setEmail(faker.internet().emailAddress(username));
        user.setPassword(faker.internet().password());
        user.setUserName(faker.name().name());
        return user;
    }

    public User selectGoogleUserByEmail(String email) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email).eq(User::getIsGoogle, "1");
        List<User> result = userDao.selectList(lqw);
        return result.get(0);
    }

    @Override
    public User login(String email, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getEmail, email);
        List<User> result = userDao.selectList(qw);
        if (result == null || result.isEmpty()) {
            throw new UserNotFoundException();
        } else if (result.get(0).getIsGoogle().equals("0")) {
            if (result.get(0).getPassword().equals(password)) {
                return result.get(0);
            } else {
                throw new PasswordNotMatchException();
            }
        } else {
            if (result.size() < 2) {
                throw new UserIsGoogleException();
            }
            if (result.get(1).getIsGoogle().equals("0")) {
                if (result.get(1).getPassword().equals(password)) {
                    return result.get(1);
                } else {
                    throw new PasswordNotMatchException();
                }
            } else {
                throw new UserIsGoogleException();
            }
        }
    }

    public User loginByGoogle(String token) {
        User user = new User();
        user.setUserName(parseJSON(token).get("name").asText());
        user.setEmail(parseJSON(token).get("email").asText());
        user.setAvatar(parseJSON(token).get("picture").asText());
        user.setPassword("google");
        user.setIsGoogle("1");
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, user.getEmail()).eq(User::getIsGoogle, "1");
        List<User> result = userDao.selectList(lqw);
        if (result.isEmpty()) {
            return user;
        } else {
            return result.get(0);
        }
    }

    public static JsonNode parseJSON(String token) {
        String[] parts = token.split("\\.", 0);


        byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
        String decodedString = new String(bytes, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(decodedString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonNode;
    }


    @Override
    public boolean save(User newUser) {
        if (newUser.getIsGoogle() == null) {
            newUser.setIsGoogle("0");
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, newUser.getEmail());
        List<User> result = userDao.selectList(lqw);
        if (newUser.getUserName() == null || newUser.getEmail() == null || newUser.getPassword() == null
                || newUser.getUserName().isEmpty() || newUser.getEmail().isEmpty() || newUser.getPassword().isEmpty()) {
            throw new ColumnIsNullException();
        }
        if (!newUser.getEmail().contains("@")) {
            throw new EmailFormNotMatchException();
        }
        if ((!result.isEmpty()) && (result.get(0).getIsGoogle().equals(newUser.getIsGoogle()))) {
            throw new EmailDuplicateException();
        }
        if (newUser.getPassword().length() < 6 || newUser.getPassword().length() > 15) {
            throw new PasswordFormNotMatchException();
        }
        return super.save(newUser);
    }


    public String getAvatarFromName(String userName) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, userName);
        User user = userDao.selectList(lqw).get(0);
        return user.getAvatar();
    }
}
