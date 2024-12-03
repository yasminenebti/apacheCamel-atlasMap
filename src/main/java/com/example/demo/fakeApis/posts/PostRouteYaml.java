package com.example.demo.fakeApis.posts;

import org.apache.camel.builder.RouteBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;

public class PostRouteYaml extends RouteBuilder {
    private String url;
    private String method;
    private Map<String, Object> headers;
    private Map<String, Object> body;
    @Override
    public void configure() throws Exception {
        loadApiConfig();
        from("timer:java?period=100000")
                .log("Starting API Call...")

                // Set headers dynamically from YAML
                .setHeader("Content-Type", constant(headers.get("Content-Type")))

                // Set body dynamically from YAML (convert the body map to JSON)
                .setBody().simple("${bean:apiBodyBean}")

                // Use HTTP method from YAML (set to POST in this case)
                .toD("${header.url}")

                .log("Body before mapping: ${body}")

                // Process using AtlasMap if required
                .to("atlasmap:post-mapper.adm")
                .log("Body after mapping: ${body}");

        /*from("timer:java?period=100000")
                .log("Starting API Call...")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("{\"title\": \"Testing Apache camel with atlas.\", \"userId\": 25}"))
                .to("https://dummyjson.com/posts/add")
                .log("Body before mapping: ${body}")
                .to("atlasmap:post-mapper.adm")
                .log("Body after mapping: ${body}");*/

    }

    private void loadApiConfig() {
        try {
            // Load YAML configuration
            FileInputStream inputStream = new FileInputStream("src/main/resources/api-config.yaml");
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);

            // Extract values from the YAML
            Map<String, Object> apiConfig = (Map<String, Object>) config.get("api");
            url = (String) apiConfig.get("url");
            method = (String) apiConfig.get("method");
            headers = (Map<String, Object>) apiConfig.get("headers");
            body = (Map<String, Object>) apiConfig.get("body");

            // Add URL as a Camel header for dynamic routing
            getContext().getGlobalOptions().put("url", url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
