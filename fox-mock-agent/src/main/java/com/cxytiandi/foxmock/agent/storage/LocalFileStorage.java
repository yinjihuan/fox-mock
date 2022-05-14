package com.cxytiandi.foxmock.agent.storage;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;
import com.cxytiandi.foxmock.agent.utils.CollectionUtils;
import com.cxytiandi.foxmock.agent.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final Logger LOG = LoggerFactory.getLogger(LocalFileStorage.class);

    private static Map<String, String> mockData = new ConcurrentHashMap<>();
    private static Map<String, Long> mockDataLastModified = new ConcurrentHashMap<>();
    private static List<String> mockMethodWhiteList = new ArrayList<>();

    @Override
    public boolean loadData(FoxMockAgentArgs request) {
        try {
            String fileDirectory = request.getFoxMockFilePath();
            if (StringUtils.isBlank(fileDirectory)) {
                LOG.info("Can not find foxMockFilePath");
                return false;
            }

            if (!whiteListIsModified(request) & !fileDirectoryIsModified(fileDirectory)) {
                return false;
            }

            // attach的场景会load多次，添加之前需要清空，否则如果文件有改动，则无法卸载掉之前的mock
            mockData.clear();

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

        return true;
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

    private void initMockDataLastModified(String fileDirectory) {
        try {
            mockDataLastModified.clear();

            Files.list(Paths.get(fileDirectory)).forEach(path -> {
                String key = path.getFileName().toString();
                mockDataLastModified.put(key, path.toFile().lastModified());
            });
        } catch (IOException e) {
            LOG.error("loadData IOException", e);
        }
    }

    private boolean fileDirectoryIsModified(String fileDirectory) {
        try {
            if (!mockDataLastModified.isEmpty()) {
                AtomicBoolean isModified = new AtomicBoolean(false);
                AtomicInteger fileCount = new AtomicInteger();

                Files.list(Paths.get(fileDirectory)).forEach(path -> {
                    fileCount.incrementAndGet();

                    String key = path.getFileName().toString();
                    Long lastModified = mockDataLastModified.get(key);
                    // 文件有改变
                    if (Objects.isNull(lastModified) || !lastModified.equals(path.toFile().lastModified())) {
                        isModified.compareAndSet(false, true);
                    }
                });

                // 文件有新增或者删除
                if (fileCount.get() != mockDataLastModified.size()) {
                    isModified.compareAndSet(false, true);
                }

                // 文件夹内没有变化
                if (!isModified.get()) {
                    return false;
                } else {
                    initMockDataLastModified(fileDirectory);
                }

            } else {
                initMockDataLastModified(fileDirectory);
            }
        } catch (IOException e) {
            LOG.error("loadData IOException", e);
        }
        return true;
    }

    private boolean whiteListIsModified(FoxMockAgentArgs request) {
        if (CollectionUtils.isEquals(mockMethodWhiteList, request.getMockMethodWhiteList())) {
            return false;
        } else {
            if (request.getMockMethodWhiteList() != null) {
                mockMethodWhiteList.clear();
                mockMethodWhiteList.addAll(request.getMockMethodWhiteList());
            }
            return true;
        }
    }
}
