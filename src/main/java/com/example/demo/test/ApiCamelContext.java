package com.example.demo.test;

import com.example.demo.customSolution.process.ConvertToDto;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class ApiCamelContext {

    public static void main(String[] args) {
        // Create camel context for MyApiHandler
        CamelContext context = new DefaultCamelContext();

        ConvertToDto convertToDto = new ConvertToDto();
        try {
            context.addRoutes(new AtlasMapper());
            context.start();
            Thread.sleep(10000);
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
