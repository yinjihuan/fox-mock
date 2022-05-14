package com.cxytiandi.foxmock.agent.utils;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-14 22:09
 */
public class ClassUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtils.class);

    public static Class<?> forNameByFormat(String className) {
        try {
            return Class.forName(formatClassName(className));
        } catch (ClassNotFoundException e) {
            LOG.error("className {} not found", className, e);
        }
        return null;
    }

    public static String formatClassName(String className) {
        return className.replace('/', '.');
    }

}
