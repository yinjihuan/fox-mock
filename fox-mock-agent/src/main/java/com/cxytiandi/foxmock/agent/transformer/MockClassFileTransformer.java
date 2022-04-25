package com.cxytiandi.foxmock.agent.transformer;

import com.cxytiandi.foxmock.agent.logger.Logger;
import com.cxytiandi.foxmock.agent.model.ClassInfo;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.utils.StringUtils;
import com.google.gson.Gson;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 22:50
 */
public class MockClassFileTransformer implements ClassFileTransformer {

    private static final Logger LOG = Logger.getLogger(MockClassFileTransformer.class);

    private static Map<String, ClassInfo> CLASS_INFO = new ConcurrentHashMap<>();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
       try {
           if (StringUtils.isBlank(className)) {
               return null;
           }

           ClassInfo classInfo = new ClassInfo(className, classfileBuffer, loader);

           // 缓存中获取没有增强之前的类的信息
           ClassInfo classInfoCache = CLASS_INFO.get(className);
           if (Objects.nonNull(classInfoCache)) {
               classInfo = new ClassInfo(classInfoCache.getClassName(), classInfoCache.getClassFileBuffer(), classInfoCache.getClassLoader());
           }

           CtClass ctClass = classInfo.getCtClass();
           ctClass.defrost();

           CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

           boolean match = false;
           for (CtMethod method : declaredMethods) {
               String methodName = method.getName();
               String key = String.format("%s#%s", classInfo.getClassName(), methodName);
               String data = StorageHelper.get(key);
               if (Objects.nonNull(data)) {
                   match = true;
                   LOG.info(String.format("mock methods %s, mock data is %s", key, data));
                   String mockCode = "if(true){com.cxytiandi.foxmock.agent.gson.Gson gson = new com.cxytiandi.foxmock.agent.gson.Gson();return ($r)gson.fromJson(%s, $type);}";
                   method.insertBefore(String.format(mockCode, new Gson().toJson(data)));
               }
           }

           // 缓存没有增强之前的类的信息
           if (match && !CLASS_INFO.containsKey(className)) {
               CLASS_INFO.put(className, new ClassInfo(className, classfileBuffer, loader));
           }

           return ctClass.toBytecode();

       } catch (Exception e) {
           throw new RuntimeException("transform error", e);
       }
    }
}
