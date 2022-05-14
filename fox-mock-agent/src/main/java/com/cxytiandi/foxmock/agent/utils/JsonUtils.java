package com.cxytiandi.foxmock.agent.utils;

import com.google.gson.Gson;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * JSON转换工具类，由于javassist不支持泛型，所以采用了一个折中的方式，在这里处理好带泛型的对象
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-06 21:20
 */
public class JsonUtils {

    private static Gson gson = new Gson();

    private JsonUtils() {}

    public static Object parse(String data, String className, String methodName) {
       try {
           Type genericReturnType = ReflectionUtils.getGenericReturnType(className, methodName);
           Object value = gson.fromJson(data, genericReturnType);
           return value;
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public static Object parseByType(String data, Type genericReturnType) {
        try {
            Object value = gson.fromJson(data, genericReturnType);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
