package com.cxytiandi.foxmock.example.dubbo;

import com.cxytiandi.foxmock.example.Result;
import com.cxytiandi.foxmock.example.UserDetail;
import com.cxytiandi.foxmock.example.UserInfo;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-18 22:54
 */
public interface DubboApi {

    UserInfo getUserInfo(Long id);

    Result<UserDetail> getUserDetail();

}
