package com.cxytiandi.foxmock.agent.transformer;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.factory.MockInfoFactory;
import com.cxytiandi.foxmock.agent.model.MockInfo;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.utils.JsonUtils;
import com.cxytiandi.foxmock.agent.utils.ReflectionUtils;
import org.apache.dubbo.rpc.*;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-18 22:42
 */
public class DubboInvokeFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DubboInvokeFilter.class);

    public static Result invoke(Object[] args) throws RpcException {
        Invoker<?> invoker = (Invoker<?>) args[0];
        Invocation invocation = (Invocation) args[1];
        String className = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        String key = String.format("%s#%s", className, methodName);
        String data = StorageHelper.get(key);
        if (Objects.nonNull(data)) {
            MockInfo mockInfo = MockInfoFactory.create(data);
            boolean filter = MethodInvokeFilter.filter(invocation.getArguments(), mockInfo.getOgnlExpress());
            if (filter) {
                LOG.info(String.format("mock methods %s, mock data is %s", key, data));
                Type genericReturnType = ReflectionUtils.getGenericReturnType(className, methodName);
                Object value = JsonUtils.parseByType(mockInfo.getMockData(), genericReturnType);
                return AsyncRpcResult.newDefaultAsyncResult(value, invocation);
            }
        }

        return null;
    }

}
