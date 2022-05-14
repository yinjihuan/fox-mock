package com.cxytiandi.foxmock.agent.transformer;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.constant.FoxMockConstant;
import com.cxytiandi.foxmock.agent.model.ClassInfo;
import com.cxytiandi.foxmock.agent.model.MockInfo;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.utils.JsonUtils;
import com.cxytiandi.foxmock.agent.utils.StringUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock 字节码增强处理
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 22:50
 */
public class MockClassFileTransformer implements ClassFileTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(MockClassFileTransformer.class);

    private static Map<String, ClassInfo> CLASS_INFO = new ConcurrentHashMap<>();

    private static Set<String> MYBATIS_MOCK_CLASS = new HashSet<String>()
    {
        {
            add(FoxMockConstant.IBATIS_BASE_EXECUTOR);
            add(FoxMockConstant.IBATIS_CACHING_EXECUTOR);
        }
    };

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

           if (ctClass.isInterface()) {
               return ctClass.toBytecode();
           }

           CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

           boolean match = false;
           for (CtMethod method : declaredMethods) {
               String methodName = method.getName();
               String key = String.format("%s#%s", classInfo.getClassName(), methodName);
               String data = StorageHelper.get(key);
               if (Objects.nonNull(data)) {
                   match = true;
                   MockInfo mockInfo = null;
                   LOG.info(String.format("mock methods %s, mock data is %s", key, data));
                   if (data.contains("f_mock_data") || data.contains("f_ognl_express")) {
                       mockInfo = JsonUtils.fromJson(data, MockInfo.class);
                   } else {
                       mockInfo = new MockInfo();
                       mockInfo.setMockData(data);
                   }
                   updateMethod(mockInfo, method, className, methodName);
               }
           }

           if (MYBATIS_MOCK_CLASS.contains(classInfo.getClassName())) {
               for (CtMethod method : declaredMethods) {
                   String methodName = method.getName();
                   if (FoxMockConstant.IBATIS_MOCK_QUERY_METHOD.equals(methodName)) {
                       method.insertBefore("Object data = com.cxytiandi.foxmock.agent.transformer.MethodInvokeFilter.filterAndConvertDataByMybatisQuery($args);if(java.util.Objects.nonNull(data)){return ($r)data;}");
                   }
                   if (FoxMockConstant.IBATIS_MOCK_UPDATE_METHOD.equals(methodName)) {
                       method.insertBefore("Object data = com.cxytiandi.foxmock.agent.transformer.MethodInvokeFilter.filterAndConvertDataByMybatisUpdate($args);if(java.util.Objects.nonNull(data)){return ($r)data;}");
                   }
               }
           }

           // 缓存没有增强之前的类的信息
           if (match && !CLASS_INFO.containsKey(className)) {
               CLASS_INFO.put(className, new ClassInfo(className, classfileBuffer, loader));
           }

           return ctClass.toBytecode();

       } catch (Exception e) {
           LOG.error("transform {} error", className, e);
           throw new RuntimeException("transform error " + className, e);
       }
    }

    private void updateMethod(MockInfo mockInfo, CtMethod method, String className, String methodName) throws CannotCompileException {
        if (mockInfo.getMockData().startsWith("throw new")) {
            String mockCode = "if(com.cxytiandi.foxmock.agent.transformer.MethodInvokeFilter.filter($args,%s)){%s}";
            method.insertBefore(String.format(mockCode, JsonUtils.toJson(mockInfo.getOgnlExpress()), mockInfo.getMockData()));
        } else {
            String mockCode = "if(com.cxytiandi.foxmock.agent.transformer.MethodInvokeFilter.filter($args,%s)){" +
                                 "return ($r)com.cxytiandi.foxmock.agent.utils.JsonUtils.parse(%s,%s,%s);" +
                              "}";
            method.insertBefore(String.format(mockCode, JsonUtils.toJson(mockInfo.getOgnlExpress()), JsonUtils.toJson(mockInfo.getMockData()), "\""+className+"\"", "\""+methodName+"\""));
        }
    }
}
