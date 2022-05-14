package com.cxytiandi.foxmock.example;

import com.cxytiandi.foxmock.example.mybatis.UserMapper;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;
import java.util.Objects;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 22:11
 */
@EnableTransactionManagement
@SpringBootApplication
public class FoxMockApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FoxMockApp.class);

        while (true) {
            UserService userService = new UserService();
            UserService.UserReq userReq = new UserService.UserReq();
            userReq.setId(2);
            System.out.println(String.format("你好 getName2 %s", userService.getName2(userReq).getId()));
            System.out.println(String.format("你好 getAge %s", userService.getAge()));
            System.out.println(String.format("你好 getAddrs %s", userService.getAddrs()));
            System.out.println(String.format("你好 getUsers %s", userService.getUsers()));
            System.out.println(String.format("你好 getUsers2 %s", userService.getUsers2()));
            System.out.println(String.format("你好 getUser %s", userService.getUser()));
            System.out.println(String.format("你好 getUserDetail %s", userService.getUserDetail()));
            // 泛型测试
            Result<UserDetail> userDetailResult = userService.getUserDetail();
            UserDetail userDetail = userDetailResult.getData();
            if (Objects.nonNull(userDetail)) {
                Map<String, UserDetail.UserAddress> addressMap = userDetail.getAddressMap();
                addressMap.forEach((k,v) -> {
                    System.out.println(k + "\t" + v.getAddress());
                });
            }
            try {
                userService.mockException("yjh");
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserMapper mapper = ApplicationContextHelper.getBean(UserMapper.class);
            System.out.println("mapper find:" + new Gson().toJson(mapper.find()));
            System.out.println("mapper findById:" + new Gson().toJson(mapper.findById(1)));
            System.out.println("mapper findNameById:" + mapper.findNameById(1));
            System.out.println("mapper updateNameById:" + mapper.updateNameById(1, "张三"));

            System.out.println("----------------------------------------");
            Thread.sleep(5000);
        }
    }

}
