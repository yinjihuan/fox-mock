package com.cxytiandi.foxmock.example;

public class UserDaoImpl implements UserDao{
    public UserInfo selectOne() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(123);
        return userInfo;
    }
}
