package com.cxytiandi.foxmock.boot;


import com.cxytiandi.foxmock.boot.config.Config;
import com.cxytiandi.foxmock.boot.utils.PathUtils;
import com.cxytiandi.foxmock.boot.utils.ProcessUtils;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Properties;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-23 21:26
 */
public class Bootstrap {

    public static void main(String[] args) {
        try {
            Properties config = Config.getConfig();

            String foxMockFilePath = config.getProperty("foxMockFilePath");
            String foxMockAgentJarPath = config.getProperty("foxMockAgentJarPath");
            String mockMethodWhiteList = config.getProperty("mockMethodWhiteList", "");

            if (Objects.isNull(foxMockAgentJarPath)) {
                foxMockAgentJarPath = PathUtils.getAgentPath() + File.separator + "fox-mock-agent-2.0.jar";
            }

            VirtualMachine attach = VirtualMachine.attach(String.valueOf(getPid()));
            String agentArgs = String.format("%s=foxMockFilePath=%s,mockMethodWhiteList=%s", foxMockAgentJarPath, foxMockFilePath, mockMethodWhiteList);
            attach.loadAgent(agentArgs);
            attach.detach();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("attach exception");
        }
    }

    private static long getPid() {
        long pid = -1;
        try {
            pid = ProcessUtils.select(false, 0, null);
        } catch (InputMismatchException e) {
            System.out.println("Please input an integer to select pid.");
            System.exit(1);
        }
        if (pid < 0) {
            System.out.println("Please select an available pid.");
            System.exit(1);
        }
        return pid;
    }

}
