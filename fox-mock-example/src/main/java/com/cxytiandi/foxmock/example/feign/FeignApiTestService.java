package com.cxytiandi.foxmock.example.feign;

import com.cxytiandi.foxmock.example.Result;
import com.cxytiandi.foxmock.example.UserDetail;
import com.cxytiandi.foxmock.example.UserInfo;
import com.cxytiandi.foxmock.example.dubbo.DubboApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-18 23:00
 */
@Service
public class FeignApiTestService {

    @Autowired
    private FeignApi feignApi;

    public UserInfo getUserInfo(Long id) {
        return feignApi.getUserInfo(id);
    }

    public Result<UserDetail> getUserDetail() {
        return feignApi.getUserDetail();
    }
}
