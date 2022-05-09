package com.cxytiandi.foxmock.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication
public class FoxMockApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FoxMockApp.class);

        while (true) {
            UserService userService = new UserService();
            System.out.println(String.format("你好 %s", userService.getName2().getId()));
            System.out.println(String.format("你好 %s", userService.getAge()));
            System.out.println(String.format("你好 %s", userService.getAddrs()));
            System.out.println(String.format("你好 %s", userService.getUsers()));
            System.out.println(String.format("你好 %s", userService.getUsers2()));
            System.out.println(String.format("你好 %s", userService.getUser()));
            System.out.println(String.format("你好 %s", userService.getUserDetail()));
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
                userService.mockException();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
            Thread.sleep(5000);
        }
    }

}
