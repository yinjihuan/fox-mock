package com.cxytiandi.foxmock.agent.utils;

import java.util.List;
import java.util.Objects;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-30 21:20
 */
public class CollectionUtils {

    public static boolean isEquals(List<String> list, List<String> list2) {
        if (Objects.isNull(list) || Objects.isNull(list2)) {
            return false;
        }

        if (list.size() != list2.size()) {
            return false;
        }

        return list.containsAll(list2);
    }
}
