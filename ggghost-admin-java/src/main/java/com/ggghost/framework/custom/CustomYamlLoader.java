package com.ggghost.framework.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 加载自定义yml
 */
public class CustomYamlLoader implements EnvironmentPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(CustomYamlLoader.class);


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            YamlPropertySourceLoader yml = new YamlPropertySourceLoader();
            PropertySource<?> p = yml.load("cutom", new ClassPathResource("customConfig.yml")).get(0);
            environment.getPropertySources().addLast(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
