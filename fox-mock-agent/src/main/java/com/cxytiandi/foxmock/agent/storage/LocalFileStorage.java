package com.cxytiandi.foxmock.agent.storage;

import com.cxytiandi.foxmock.agent.logger.Logger;
import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;
import com.cxytiandi.foxmock.agent.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 本地文件存储
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 23:46
 */
public class LocalFileStorage implements Storage {

    private static final Logger LOG = Logger.getLogger(LocalFileStorage.class);

    private static Map<String, String> mockData = new ConcurrentHashMap<>();

    @Override
    public void loadData(FoxMockAgentArgs request) {
        try {
            // attach的场景会load多次，添加之前需要清空，否则如果文件有改动，则无法卸载掉之前的mock
            mockData.clear();

            String fileDirectory = request.getFoxMockFilePath();
            if (StringUtils.isBlank(fileDirectory)) {
                LOG.info("Can not find foxMockFilePath");
                return;
            }

            Files.list(Paths.get(fileDirectory)).forEach(path -> {
                String key = path.getFileName().toString();
                if (request.getMockMethodWhiteList() != null && !request.getMockMethodWhiteList().isEmpty()) {
                    if (request.getMockMethodWhiteList().contains(key)) {
                        mockData.put(key, readFileContent(path));
                    }
                } else {
                    mockData.put(key, readFileContent(path));
                }

            });
        } catch (IOException e) {
            LOG.error("loadData IOException", e);
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

    private String readFileContent(Path path) {
        try {
            String content = new String(Files.readAllBytes(path), Charset.forName("UTF-8"));
            return content;
        } catch (IOException e) {
            LOG.error(String.format("readFileContent IOException, path is {}", path.toString()), e);
        }
        return null;
    }
}
