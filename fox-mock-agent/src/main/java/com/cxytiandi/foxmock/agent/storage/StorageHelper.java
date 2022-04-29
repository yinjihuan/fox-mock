package com.cxytiandi.foxmock.agent.storage;

import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public static void loadAllData(FoxMockAgentArgs request) {
        storageList.forEach(storage -> storage.loadData(request));
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
        return storageList.stream().map(storage -> {
            return storage.getMockClassNames();
        }).flatMap(Set::stream).collect(Collectors.toSet());
    }
}
