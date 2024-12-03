package com.example.demo.customSolution;

import com.example.demo.customSolution.process.LoadYamlProcessor;
import com.example.demo.customSolution.process.ProcessApiRoutesProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class ApacheCmelUtil {
    public static void main(String[] args) throws Exception {
        // Create instances of your processors
        LoadYamlProcessor loadYamlProcessor = new LoadYamlProcessor();
        ProcessApiRoutesProcessor processApiRoutesProcessor = new ProcessApiRoutesProcessor();

        // 1. Create CamelContext
        CamelContext camelContext = new DefaultCamelContext();

        // 2. Create Routes and pass the processors to the ApiRoute constructor
        ApiRoute apiRoute = new ApiRoute(loadYamlProcessor, processApiRoutesProcessor);
        camelContext.addRoutes(apiRoute);

        // 3. Start the CamelContext
        camelContext.start();

        // Keep the application running for some time to test
        Thread.sleep(5000);

        // Stop the CamelContext
        camelContext.stop();
    }
}

