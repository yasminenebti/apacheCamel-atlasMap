package com.example.demo.fakeApis.toDo;

import org.apache.camel.builder.RouteBuilder;

public class ToDoRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:java?period=100000")
                .log("Starting API Call...")
                .to("https://jsonplaceholder.typicode.com/todos/1")
                .process(exchange -> {
                    Object body = exchange.getIn().getBody();
                    System.out.println("Body after conversion: " + body);
                    System.out.println("Body type after conversion: " + body.getClass().getName());
                    //log body content
                    System.out.println("Body content: " + body);

                })
                .to("atlasmap:todo-mapping.adm")
                .log("Body before mapping: ${body}");

    }
}
