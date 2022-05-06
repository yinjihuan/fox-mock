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

    public static Object parse(String data, String className, String methodName) {
       try {
           Type genericReturnType = null;
           Class<?> clazz = Class.forName(className.replace('/','.'));
           Method[] declaredMethods = clazz.getDeclaredMethods();
           for (Method m: declaredMethods) {
               if (m.getName().equals(methodName)){
                   genericReturnType = m.getGenericReturnType();
                   break;
               }
           }

           Gson gson = new Gson();
           Object value = gson.fromJson(data, genericReturnType);
           return value;
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
