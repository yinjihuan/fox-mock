package com.cxytiandi.foxmock.example.dubbo;

import com.cxytiandi.foxmock.example.Result;
import com.cxytiandi.foxmock.example.UserDetail;
import com.cxytiandi.foxmock.example.UserInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-18 23:00
 */
@Service
public class DubboApiTestService {

    @Reference(url="dubbo://localhost:20880")
    private DubboApi dubboApi;

    public UserInfo getUserInfo(Long id) {
        return dubboApi.getUserInfo(id);
    }

    public Result<UserDetail> getUserDetail() {
        return dubboApi.getUserDetail();
    }
}
