package com.cxytiandi.foxmock.example.feign;

import com.cxytiandi.foxmock.example.Result;
import com.cxytiandi.foxmock.example.UserDetail;
import com.cxytiandi.foxmock.example.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-24 22:59
 */
@FeignClient(name = "fox-mock-example", url = "http://localhost:8080")
public interface FeignApi {

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    UserInfo getUserInfo(@RequestParam("id")Long id);

    @RequestMapping(value = "/getUserDetail", method = RequestMethod.GET)
    Result<UserDetail> getUserDetail();

}
