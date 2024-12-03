package com.example.demo.customSolution.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class LoadYamlProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        InputStream yamlStream = getClass().getClassLoader().getResourceAsStream("api.yaml");

        if (yamlStream == null) {
            throw new RuntimeException("YAML file 'api.yaml' not found in the resources folder.");
        }

        // Parse YAML into a Map
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(yamlStream);
        exchange.getIn().setBody(yamlMap);

        log.info("YAML content successfully loaded.");
    }
}

