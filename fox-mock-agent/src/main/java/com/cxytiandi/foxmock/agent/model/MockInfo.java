package com.cxytiandi.foxmock.agent.model;

/**
 * Mock信息
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-11 21:47
 */
public class MockInfo {

    /**
     * Mock 数据
     */
    private String f_mock_data;

    /**
     * ognl匹配表达式
     */
    private String f_ognl_express;

    public void setMockData(String mockData) {
        this.f_mock_data = mockData;
    }

    public String getMockData() {
        return f_mock_data;
    }

    public void setOgnlExpress(String ognlExpress) {
        this.f_ognl_express = ognlExpress;
    }

    public String getOgnlExpress() {
        return f_ognl_express;
    }
}
