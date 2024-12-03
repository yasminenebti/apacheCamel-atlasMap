package com.example.demo.fakeApis.posts;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class PostContext {
    public static void main(String[] args) {
        // Create camel context for StarRoute
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new PostRoute());
            context.start();
            Thread.sleep(10000);
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
