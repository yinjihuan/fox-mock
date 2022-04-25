package com.cxytiandi.foxmock.agent.model;

import com.cxytiandi.foxmock.agent.utils.StringUtils;

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

    private String foxMockFilePath;

    public FoxMockAgentArgs(String agentArgs) {
        parseArgs(agentArgs);
    }

    private void parseArgs(String agentArgs) {
        if (!StringUtils.isBlank(agentArgs)) {
            String[] args = agentArgs.split(",");
            for (String arg : args) {
                String key = arg.split("=")[0];
                String value = arg.split("=")[1];
                if ("foxMockFilePath".equals(key)) {
                    this.foxMockFilePath = value;
                }
            }
        }
    }

    public void setFoxMockFilePath(String foxMockFilePath) {
        this.foxMockFilePath = foxMockFilePath;
    }

    public String getFoxMockFilePath() {
        return foxMockFilePath;
    }
}
