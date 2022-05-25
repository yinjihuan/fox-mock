package com.cxytiandi.foxmock.example.feign;

import com.cxytiandi.foxmock.example.Result;
import com.cxytiandi.foxmock.example.UserDetail;
import com.cxytiandi.foxmock.example.UserInfo;
import org.springframework.web.bind.annotation.*;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-24 22:59
 */
@RestController
public class FeignApiImpl {

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public UserInfo getUserInfo(@RequestParam("id")Long id) {
        return new UserInfo();
    }

    @RequestMapping(value = "/getUserDetail", method = RequestMethod.GET)
    public Result<UserDetail> getUserDetail() {
        return new Result<>();
    }
}
