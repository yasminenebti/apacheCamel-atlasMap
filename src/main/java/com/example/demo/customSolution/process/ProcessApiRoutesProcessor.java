package com.example.demo.customSolution.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProcessApiRoutesProcessor implements Processor {
    private static final String OUTPUT_DIR = "src/main/java/com/example/demo";



    @Override
    public void process(Exchange exchange) throws Exception {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));


        // Get the parsed YAML content from the exchange
        Map<String, Object> yamlMap = exchange.getIn().getBody(Map.class);
        Map<String, Object> apiData = (Map<String, Object>) yamlMap.get("api");

        String baseUrl = (String) apiData.get("baseUrl");
        String packageName = "com.example.demo";
        List<Map<String, Object>> endpoints = (List<Map<String, Object>>) apiData.get("endpoints");

        Template template = cfg.getTemplate("apiClient.ftl");




        generateClientFile(template, apiData, endpoints, baseUrl, packageName);
        log.info("API routes processed.");
    }

    private static void generateClientFile(Template template, Map<String, Object> apiData, List<Map<String, Object>> endpoints, String baseUrl, String packageName)
            throws IOException, TemplateException {

        // Prepare data model for FreeMarker template
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", packageName);
        templateData.put("baseUrl", baseUrl);
        templateData.put("api", apiData);
        templateData.put("endpoints", endpoints);

        // Define output file
        String className = apiData.get("name") + "Client";
        File outputFile = new File(OUTPUT_DIR, className + ".java");

        // Write data to output file using template
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(templateData, writer);
        }
    }
}

