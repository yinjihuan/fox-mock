package com.cxytiandi.foxmock.boot.config;


import com.cxytiandi.foxmock.boot.utils.PathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {

    private static Properties config;

    public static void initializeCoreConfig() {
        config = new Properties();

        try (final InputStreamReader configFileStream = loadConfig()) {
            config.load(configFileStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStreamReader loadConfig() {
        File configFile = new File(PathUtils.getAgentPath(), "agent.properties");

        if (configFile.exists() && configFile.isFile()) {
            try {
                return new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("Failed to load agent.config");
    }

    public static Properties getConfig() {
        if (null == config) {
            initializeCoreConfig();
        }

        return config;
    }


}