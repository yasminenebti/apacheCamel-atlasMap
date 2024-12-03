package com.example.demo.star.contract;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class StarContractContext {
    public static void main(String[] args) {
        // Create camel context for StarRoute
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new StarContractRoute());
            context.start();
            Thread.sleep(10000);
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
