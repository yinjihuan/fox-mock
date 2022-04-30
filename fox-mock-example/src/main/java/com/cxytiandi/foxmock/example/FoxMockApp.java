package com.cxytiandi.foxmock.example;


/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 22:11
 */
public class FoxMockApp {

    public static void main(String[] args) throws Exception {
       while (true) {
           UserService userService = new UserService();
           System.out.println(String.format("你好 %s", userService.getName2().getId()));
           System.out.println(String.format("你好 %s", userService.getAge()));
           System.out.println(String.format("你好 %s", userService.getAddrs()));
           System.out.println(String.format("你好 %s", userService.getUsers()));
           System.out.println(String.format("你好 %s", userService.getUsers2()));
           System.out.println(String.format("你好 %s", userService.getUser()));
           System.out.println(String.format("你好 %s", userService.getUserDetail()));
           System.out.println("----------------------------------------");
           Thread.sleep(5000);
       }

    }

}
