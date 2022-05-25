package feign;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.factory.MockInfoFactory;
import com.cxytiandi.foxmock.agent.model.MockInfo;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.transformer.MethodInvokeFilter;
import com.cxytiandi.foxmock.agent.utils.JsonUtils;
import com.cxytiandi.foxmock.agent.utils.ReflectionUtils;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-05-24 23:07
 */
public class FeignInvokeFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FeignInvokeFilter.class);

    public static Object invoke(Object[] argsArray, Object methodHandler) throws Throwable {
        Object[] args = (Object[]) argsArray[0];
        SynchronousMethodHandler handler = (SynchronousMethodHandler) methodHandler;
        Class<? extends SynchronousMethodHandler> handlerClass = handler.getClass();

        MethodMetadata methodMetadata = (MethodMetadata) ReflectionUtils.getFieldValue("metadata", handlerClass, handler);
        Target<?> target = (Target<?>) ReflectionUtils.getFieldValue("target", handlerClass, handler);;

        String className = target.type().getName();
        String ms = methodMetadata.configKey().split("#")[1];
        String methodName = ms.substring(0, ms.indexOf("("));

        String key = String.format("%s#%s", className, methodName);
        String data = StorageHelper.get(key);

        if (Objects.nonNull(data)) {
            MockInfo mockInfo = MockInfoFactory.create(data);
            boolean filter = MethodInvokeFilter.filter(args, mockInfo.getOgnlExpress());
            if (filter) {
                LOG.info(String.format("mock methods %s, mock data is %s", key, data));
                Type genericReturnType = methodMetadata.returnType();
                Object value = JsonUtils.parseByType(mockInfo.getMockData(), genericReturnType);
                return value;
            }
        }

        return null;
    }
}
