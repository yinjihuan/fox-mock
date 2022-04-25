package com.cxytiandi.foxmock.boot.utils;

import java.io.File;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-25 22:34
 */
public class PathUtils {

    public static String getAgentPath() {
        String path = new File("").getAbsolutePath();
        return path;
    }
}
