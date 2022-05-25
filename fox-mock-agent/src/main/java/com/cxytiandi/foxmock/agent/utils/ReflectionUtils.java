package com.cxytiandi.foxmock.agent.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-14 22:37
 */
public class ReflectionUtils {

    public static Type getGenericReturnType(String className, String methodName) {
        Type genericReturnType = null;

        Class<?> clazz = ClassUtils.forNameByFormat(className);
        if (Objects.isNull(clazz)) {
            return null;
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m: declaredMethods) {
            if (m.getName().equals(methodName)){
                genericReturnType = m.getGenericReturnType();
                break;
            }
        }

        return genericReturnType;
    }

    public static Object getFieldValue(String fieldName, Class<?> clz, Object target) throws Throwable {
        Field field = clz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
