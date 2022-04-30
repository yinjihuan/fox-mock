package com.cxytiandi.foxmock.agent.model;

import com.cxytiandi.foxmock.agent.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * agent参数
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-24 22:41
 */
public class FoxMockAgentArgs {


    /**
     * mock文件路径
     */
    private String foxMockFilePath;

    /**
     * mock方法白名单,如果文件夹中有多个方法会被全部mock,如果指定了此配置将只会mock这里指定的方法
     * 格式: com.xx.xxService#getName|com.xx.xxService#getAge
     */
    private List<String> mockMethodWhiteList;

    /**
     * mock数据通过http请求获取
     */
    private String mockDataHttpUrl;

    public FoxMockAgentArgs(String agentArgs) {
        parseArgs(agentArgs);
    }

    private void parseArgs(String agentArgs) {
        if (!StringUtils.isBlank(agentArgs)) {
            String[] args = agentArgs.split(",");
            for (String arg : args) {
                String[] values = arg.split("=");
                if (values.length != 2) {
                    continue;
                }
                String key = values[0];
                String value = values[1];
                if ("foxMockFilePath".equals(key)) {
                    this.foxMockFilePath = value;
                }
                if ("mockMethodWhiteList".equals(key)) {
                    this.mockMethodWhiteList = new ArrayList<>(Arrays.asList(value.split("\\|")));
                }
                if ("mockDataHttpUrl".equals(key)) {
                    this.mockDataHttpUrl = decodeUrl(value);
                }
            }
        }
    }

    private String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(e);
        }
    }

    public String getFoxMockFilePath() {
        return foxMockFilePath;
    }

    public List<String> getMockMethodWhiteList() {
        return mockMethodWhiteList;
    }

    public String getMockDataHttpUrl() {
        return mockDataHttpUrl;
    }
}
