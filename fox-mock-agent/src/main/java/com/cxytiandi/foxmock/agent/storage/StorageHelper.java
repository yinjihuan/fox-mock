package com.cxytiandi.foxmock.agent.storage;

import com.cxytiandi.foxmock.agent.constant.FoxMockConstant;
import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 存储帮助类
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-19 22:08
 */
public class StorageHelper {

    static List<Storage> storageList = new ArrayList<>();

    static {
        storageList.add(new LocalFileStorage());
        storageList.add(new HttpStorage());
    }

    static Set<String> allMockClassNames = new HashSet<>();

    /**
     * 加载所有数据
     * @param request
     * @return
     */
    public static boolean loadAllData(FoxMockAgentArgs request) {
        int successCount = 0;
        for (Storage storage : storageList) {
            boolean result = storage.loadData(request);
            if (result) {
                successCount++;
            }
        }
        return successCount > 0;
    }

    /**
     * 获取mock数据
     * @param key 格式：方法全路径 com.xx.User#getName
     * @return
     */
    public static String get(String key) {
        for (Storage storage : storageList) {
            String data = storage.get(key);
            if (Objects.nonNull(data)) {
                return data;
            }
        }
        return null;
    }

    /**
     * 获取mock的类名称
     * @return
     */
    public static Set<String> getMockClassNames() {
        Set<String> mockClassNames = storageList.stream().map(storage -> {
            return storage.getMockClassNames();
        }).flatMap(Set::stream).collect(Collectors.toSet());

        allMockClassNames.addAll(mockClassNames);
        allMockClassNames.add(FoxMockConstant.IBATIS_BASE_EXECUTOR);
        allMockClassNames.add(FoxMockConstant.IBATIS_CACHING_EXECUTOR);
        allMockClassNames.add(FoxMockConstant.DUBBO_CONSUMER_FILTER);
        allMockClassNames.add(FoxMockConstant.FEIGN_METHOD_HANDLER);
        return allMockClassNames;
    }
}
