package com.example.demo.fakeApis;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ConfigLoader {
    public static ApiConfig loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.yaml")) {
            return yaml.loadAs(input, ApiConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

}
