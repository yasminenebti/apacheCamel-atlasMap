package com.example.demo.test;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@RequiredArgsConstructor
public class AtlasMap extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:java?period=100000")
                .setBody()
                .simple("resource:classpath:order.json")
                .unmarshal().json().log("${body}")
                .marshal().json()
                .to("atlasmap:atlas.adm")
                .log("${body}");
                //.unmarshal().json(JsonLibrary.Jackson, Map.class) // Convert result to Java Map
                //.process(new ConvertToDto());
    }
}



//from("timer:java?period=100000")
//                .setBody()
//                .simple("classpath:order.json")
//                .log("--&gt; Sending message: ${body}")
//                .unmarshal().json()
//                .to("atlasmap:atlasmap-mapping2.adm")
//                .log("--&lt; Received message: [${body}]");