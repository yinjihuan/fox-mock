package com.cxytiandi.foxmock.example;

import com.cxytiandi.foxmock.example.dubbo.DubboApiTestService;
import com.cxytiandi.foxmock.example.feign.FeignApiTestService;
import com.cxytiandi.foxmock.example.mybatis.UserMapper;
import com.cxytiandi.foxmock.example.mybatis.UserQuery;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableFeignClients(basePackages = "com.cxytiandi.foxmock.example.feign")
@EnableTransactionManagement
@SpringBootApplication
public class FoxMockApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FoxMockApp.class);

        while (true) {
           /* UserService userService = new UserService();
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
            System.out.println("mapper find2:" + new Gson().toJson(mapper.find2(new UserQuery(1))));
            System.out.println("mapper findById:" + new Gson().toJson(mapper.findById(1)));
            System.out.println("mapper findNameById:" + mapper.findNameById(1));
            System.out.println("mapper updateNameById:" + mapper.updateNameById(1, "张三"));
*/
            DubboApiTestService dubboApiTestService = ApplicationContextHelper.getBean(DubboApiTestService.class);
            System.out.println("dubbo getUserInfo:" + new Gson().toJson(dubboApiTestService.getUserInfo(1L)));
            Result<UserDetail> userDetailResult1 = dubboApiTestService.getUserDetail();
            System.out.println("dubbo getUserDetail:" + new Gson().toJson(userDetailResult1));
            UserDetail userDetail1 = userDetailResult1.getData();
            if (Objects.nonNull(userDetail1)) {
                Map<String, UserDetail.UserAddress> addressMap = userDetail1.getAddressMap();
                addressMap.forEach((k,v) -> {
                    System.out.println(k + "\t" + v.getAddress());
                });
            }

            FeignApiTestService feignApiTestService = ApplicationContextHelper.getBean(FeignApiTestService.class);
            System.out.println("feign getUserInfo:" + new Gson().toJson(feignApiTestService.getUserInfo(1L)));
            userDetailResult1 = feignApiTestService.getUserDetail();
            System.out.println("feign getUserDetail:" + new Gson().toJson(userDetailResult1));
            userDetail1 = userDetailResult1.getData();
            if (Objects.nonNull(userDetail1)) {
                Map<String, UserDetail.UserAddress> addressMap = userDetail1.getAddressMap();
                addressMap.forEach((k,v) -> {
                    System.out.println(k + "\t" + v.getAddress());
                });
            }

            System.out.println("----------------------------------------");
            Thread.sleep(5000);
        }
    }

}
