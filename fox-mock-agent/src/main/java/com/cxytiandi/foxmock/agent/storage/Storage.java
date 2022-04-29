package com.cxytiandi.foxmock.agent.storage;

import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;

import java.util.Set;

/**
 * mock数据存储接口
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 23:46
 */
public interface Storage {

    /**
     * 加载数据
     * @param request
     */
    boolean loadData(FoxMockAgentArgs request);

    /**
     * 获取数据
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取mock的类名称
     * @return
     */
    Set<String> getMockClassNames();

}
