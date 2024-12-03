package com.example.demo.star.beneficary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.tomcat.jni.Library;

public class StarBeneficiaryRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:java?period=100000")
                .log("Starting API Call...")
                .setHeader("x-api-key", constant("SVpJQVBJS0VZIzIwMjMwMjI4"))
                .setHeader("X-fern-token", constant("qcxv8ebS6cyhMzIpEFrA2zSCc"))
                .to("http://10.20.0.242/MYSTAR.APIS/api/v2/MySTAR/GetMappingBeneficiairesVie?codeSousBranche=687")
                .doTry()
                .to("atlasmap:schema.adm")
                .log("Mapped Output: ${body}")
                .doCatch(Exception.class)
                .log("Error during mapping: ${exception.message}")
                .end();



        /*from("timer:java?period=100000")
                .log("Starting API Call...")
                .setHeader("x-api-key", constant("SVpJQVBJS0VZIzIwMjMwMjI4"))
                .setHeader("X-fern-token", constant("qcxv8ebS6cyhMzIpEFrA2zSCc"))
                .to("https://10.20.0.242/MYSTAR.APIS/api/v2/MySTAR/GetMappingBeneficiairesVie?codeSousBranche=687"
                        + "&httpMethod=GET"
                        + "&header.x-api-key=SVpJQVBJS0VZIzIwMjMwMjI4"  // Add x-api-key header
                        + "&header.X-fern-token=qcxv8ebS6cyhMzIpEFrA2zSCc")
                .process(exchange -> {
                    Object body = exchange.getIn().getBody();
                    System.out.println("Body after conversion: " + body);
                    System.out.println("Body type after conversion: " + body.getClass().getName());

                })
                .to("atlasmap:star-beneficiary.adm")
                .log("Mapped Output: ${body}");*/

    }
}
