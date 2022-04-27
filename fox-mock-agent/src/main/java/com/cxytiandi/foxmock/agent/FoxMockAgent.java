package com.cxytiandi.foxmock.agent;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.cxytiandi.foxmock.agent.model.FoxMockAgentArgs;
import com.cxytiandi.foxmock.agent.storage.StorageHelper;
import com.cxytiandi.foxmock.agent.transformer.MockClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Set;

/**
 * Mock 入口
 *
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-18 22:17
 */
public class FoxMockAgent {

    private static final Logger LOG = LoggerFactory.getLogger(FoxMockAgent.class);

    public static void premain(final String agentArgs, final Instrumentation inst) {
        LOG.info(String.format("[FoxMockAgent.premain] begin, agentArgs: %s, Instrumentation:%s", agentArgs, inst));
        main(agentArgs, inst);
    }

    public static void agentmain(final String agentArgs, final Instrumentation inst) {
        LOG.info(String.format("[FoxMockAgent.agentmain] begin, agentArgs: %s, Instrumentation:%s", agentArgs, inst));
        main(agentArgs, inst);
    }

    private static void main(final String agentArgs, final Instrumentation inst) {
        StorageHelper.loadAllData(new FoxMockAgentArgs(agentArgs));

        MockClassFileTransformer mockClassFileTransformer = new MockClassFileTransformer();
        inst.addTransformer(mockClassFileTransformer, true);

        Set<String> mockClassNames = StorageHelper.getMockClassNames();
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class clz : allLoadedClasses) {
            if (mockClassNames.contains(clz.getName())) {
                try {
                    inst.retransformClasses(clz);
                } catch (UnmodifiableClassException e) {
                    LOG.error("retransformClasses exception", e);
                    throw new RuntimeException("retransformClasses exception", e);
                }
            }
        }

        LOG.info("foxMock agent run completely");
    }
}
