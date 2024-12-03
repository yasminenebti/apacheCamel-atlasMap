package com.example.demo.fakeApis.posts;

import com.example.demo.fakeApis.ApiConfig;
import com.example.demo.fakeApis.ConfigLoader;
import org.apache.camel.builder.RouteBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;

public class PostRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:java?period=100000")
                .log("Starting API Call...")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("{\"title\": \"Testing Apache camel with atlas.\", \"userId\": 25}"))
                .to("https://dummyjson.com/posts/add")
                .log("Body before mapping: ${body}")
                .to("atlasmap:post-mapper.adm")
                .log("Body after mapping: ${body}");

    }
}
