package com.example.demo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;

public class StarApiHandler extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:apiLogger?period=600000")
                .routeId("logApiRequest")
                .log("Starting API call to get contract assistance details...")

                // Fetch data from API
                .to("http://10.20.0.42:64/contrat_assistance/get-by-identContrat?identProcess=fe25d109-5f23-11ef-8dc7-b64d84729be6"
                        + "&httpMethod=GET"
                        + "&header.accept=*/*")
                .log("Raw API Response: ${body}")

                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.setProperty("rawResponse", body); // Store raw response
                    exchange.getIn().setBody(body); // Ensure body is preserved
                })
                .log("Raw API Response: ${property.rawResponse}")

                // Validate JSON
                .process(exchange -> {
                    String body = exchange.getProperty("rawResponse", String.class); // Use raw response
                    if (body == null || body.trim().isEmpty()) {
                        throw new RuntimeException("Body is null or empty!");
                    }
                    try {
                        new ObjectMapper().readTree(body); // Validate JSON
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Invalid JSON format: " + e.getMessage());
                    }
                })
                .log("Validated JSON: ${property.rawResponse}")


                // Unmarshal and process
                .unmarshal().json()
                .log("Unmarshalled JSON: ${body}")
                .marshal().json()
                .process(new JsonCleanerProcessor())
                .to("atlasmap:my-star-mapping.adm")
                .log("Mapped JSON: ${body}");
    }
    }
