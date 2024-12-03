package com.example.demo.customSolution;

import com.example.demo.customSolution.process.LoadYamlProcessor;
import com.example.demo.customSolution.process.ProcessApiRoutesProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRoute extends RouteBuilder {

    private final LoadYamlProcessor loadYamlProcessor;
    private final ProcessApiRoutesProcessor processApiRoutesProcessor;

    @Override
    public void configure() throws Exception {
        from("timer:yaml-loader?repeatCount=1") // Trigger once to load the YAML file
                .routeId("LoadApiYamlRoute")
                .log("Timer triggered, starting YAML load...")
                .process(loadYamlProcessor)  // Using the custom processor to load YAML
                .log("Parsed YAML content: ${body}")
                .log("YAML validation passed successfully.")
                .process(processApiRoutesProcessor) // Using the custom processor to process APIs
                .log("API routes processed.");
    }
}
