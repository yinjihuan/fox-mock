package com.cxytiandi.foxmock.agent.factory;

import com.cxytiandi.foxmock.agent.model.MockInfo;
import com.cxytiandi.foxmock.agent.utils.JsonUtils;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-15 22:59
 */
public class MockInfoFactory {

    public static MockInfo create(String data) {
        MockInfo mockInfo = null;
        if (data.contains("f_mock_data") || data.contains("f_ognl_express")) {
            mockInfo = JsonUtils.fromJson(data, MockInfo.class);
        } else {
            mockInfo = new MockInfo();
            mockInfo.setMockData(data);
        }
        return mockInfo;
    }
}
