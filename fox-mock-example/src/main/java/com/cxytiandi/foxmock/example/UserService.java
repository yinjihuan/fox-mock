package com.cxytiandi.foxmock.example;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-20 00:25
 */
public class UserService {
    public UserInfo getName2(UserReq req) {
        return new UserInfo();
    }

    public Integer getAge() {
        return 0;
    }

    public List<String> getAddrs() {
        return new ArrayList<>();
    }

    public List<UserInfo> getUsers() {
        return new ArrayList<>();
    }

    public Result<List<UserInfo>> getUsers2() {
        Result<List<UserInfo>> result = new Result<>();
        result.setData(new ArrayList<>());
        return result;
    }

    public Result<UserInfo> getUser() {
        Result<UserInfo> result = new Result<>();
        return result;
    }

    public Result<UserDetail> getUserDetail() {
        Result<UserDetail> result = new Result<>();
        return result;
    }

    public void mockException(String name) {

    }

    public static class UserReq {
        private int id;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
