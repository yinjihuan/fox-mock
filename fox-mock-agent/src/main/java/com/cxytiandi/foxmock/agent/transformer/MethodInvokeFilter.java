package com.cxytiandi.foxmock.agent.transformer;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.express.Express;
import com.cxytiandi.foxmock.agent.express.OgnlExpressException;
import com.cxytiandi.foxmock.agent.express.ExpressFactory;
import com.cxytiandi.foxmock.agent.factory.MockInfoFactory;
import com.cxytiandi.foxmock.agent.model.MockInfo;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.utils.JsonUtils;
import com.cxytiandi.foxmock.agent.utils.ReflectionUtils;
import com.cxytiandi.foxmock.agent.utils.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 方法执行过滤
 * <p>采用ognl进行mock方法的匹配，如果只想对指定参数进行mock就配置ognl表达式<p>
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-10 21:54
 */
public class MethodInvokeFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MethodInvokeFilter.class);

    public static boolean filter(Object[] args, String express) {
        if (args.length == 0 || StringUtils.isBlank(express)) {
            return true;
        }

        Express ex = ExpressFactory.getExpress(args);
        for (int i = 0; i < args.length; i++) {
            ex.bind("p" + i, args[i]);
        }

        try {
            return ex.is(express);
        } catch (OgnlExpressException e) {
            LOG.error("ognl filter exception", e);
        }

        return false;
    }

    public static Object filterAndConvertDataByMybatisQuery(Object[] args) {
        Object obj = filterAndConvertData(args);
        if (obj == null) {
            return null;
        }

        // 集合直接返回
        if (obj instanceof List) {
           return obj;
        }

        // 非集合包装成集合返回，因为org.apache.ibatis.executor.BaseExecutor.query返回的是集合
        List<Object> objs = new ArrayList<>();
        objs.add(obj);
        return objs;
    }

    public static Object filterAndConvertDataByMybatisUpdate(Object[] args) {
        return filterAndConvertData(args);
    }

    private static Object filterAndConvertData(Object[] args) {
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        String msId = ms.getId();
        String className = msId.substring(0, msId.lastIndexOf("."));
        String methodName = msId.substring(msId.lastIndexOf(".") + 1);
        String key = String.format("%s#%s", className, methodName);

        String data = StorageHelper.get(key);
        if (Objects.isNull(data)) {
            return null;
        }

        List<Object> argsList = new ArrayList<>();
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        for (ParameterMapping mapping : parameterMappings) {
            if (parameterObject instanceof HashMap) {
                HashMap parameterMap = (HashMap) parameterObject;
                Object value = parameterMap.get(mapping.getProperty());
                argsList.add(value);
            } else {
                argsList.add(parameterObject);
            }
        }

        MockInfo mockInfo = MockInfoFactory.create(data);
        boolean filter = filter(argsList.toArray(new Object[argsList.size()]), mockInfo.getOgnlExpress());
        if (filter) {
            LOG.info(String.format("mock methods %s, mock data is %s", key, data));
            Type genericReturnType = ReflectionUtils.getGenericReturnType(className, methodName);
            Object obj = JsonUtils.parseByType(mockInfo.getMockData(), genericReturnType);
            return obj;
        }

        return null;
    }
}
