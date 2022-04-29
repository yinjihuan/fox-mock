package com.cxytiandi.foxmock.agent.storage;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;
import com.cxytiandi.foxmock.agent.utils.HttpUtils;
import com.cxytiandi.foxmock.agent.utils.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Http 请求获取mock数据
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-29 21:31
 */
public class HttpStorage implements Storage {

    private static final Logger LOG = LoggerFactory.getLogger(HttpStorage.class);

    private static Map<String, String> mockData = new ConcurrentHashMap<>();

    @Override
    public void loadData(FoxMockAgentArgs request) {
        if (StringUtils.isBlank(request.getMockDataHttpUrl())) {
            return;
        }

        String mockDataText = HttpUtils.get(request.getMockDataHttpUrl());

        if (!StringUtils.isBlank(mockDataText)) {
            // attach的场景会load多次，添加之前需要清空，否则如果文件有改动，则无法卸载掉之前的mock
            mockData.clear();
        }

        Properties properties = new Properties();
        try {
            properties.load(new StringReader(mockDataText));
            Set<String> keys = properties.stringPropertyNames();
            for (String key : keys) {
                String value = properties.getProperty(key);
                mockData.put(key, value);
            }
        } catch (IOException e) {
            LOG.error("loadData IOException, url is {}", request.getMockDataHttpUrl(), e);
        } catch (Exception e) {
            LOG.error("loadData Exception url is {}", request.getMockDataHttpUrl(), e);
        }
    }

    @Override
    public String get(String key) {
        return mockData.get(key);
    }

    @Override
    public Set<String> getMockClassNames() {
        return mockData.keySet().stream().map(key -> key.split("#")[0]).collect(Collectors.toSet());
    }

}
