package com.cxytiandi.foxmock.agent.transformer;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.express.Express;
import com.cxytiandi.foxmock.agent.express.OgnlExpressException;
import com.cxytiandi.foxmock.agent.express.ExpressFactory;
import com.cxytiandi.foxmock.agent.utils.StringUtils;

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

    private static final Logger LOG = LoggerFactory.getLogger(MockClassFileTransformer.class);

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
}
