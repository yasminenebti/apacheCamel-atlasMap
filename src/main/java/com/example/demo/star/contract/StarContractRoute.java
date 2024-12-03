package com.example.demo.star.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.builder.RouteBuilder;

public class StarContractRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:java?period=100000")
                .log("Starting API Call...")
                .to("http://10.20.0.42:64/contrat_assistance/get-by-identContrat?identProcess=fe25d109-5f23-11ef-8dc7-b64d84729be6"
                        + "&httpMethod=GET"
                        + "&header.accept=*/*")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);

                    // Step 1: Convert the listeDoc field into a proper JSON array if necessary
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(body);

                        // If listeDoc is a string, parse it as JSON
                        if (rootNode.has("listeDoc")) {
                            String listeDocStr = rootNode.get("listeDoc").asText();
                            JsonNode listeDocArray = objectMapper.readTree(listeDocStr); // Convert the string into an array
                            ((ObjectNode) rootNode).set("listeDoc", listeDocArray);  // Set the correct JSON array in the root node
                        }

                        // Step 2: Fix the date format (optional step if needed)
                        if (rootNode.has("dateEffet")) {
                            String dateEffet = rootNode.get("dateEffet").asText();
                            if (dateEffet != null && dateEffet.matches("\\d{2}/\\d{2}/\\d{4}")) {
                                // Convert the date format to YYYY-MM-DD
                                String[] dateParts = dateEffet.split("/");
                                String newDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                                ((ObjectNode) rootNode).put("dateEffet", newDate);
                            }
                        }
                        if (rootNode.has("dateEcheance")) {
                            String dateEcheance = rootNode.get("dateEcheance").asText();
                            if (dateEcheance != null && dateEcheance.matches("\\d{2}/\\d{2}/\\d{4}")) {
                                // Convert the date format to YYYY-MM-DD
                                String[] dateParts = dateEcheance.split("/");
                                String newDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                                ((ObjectNode) rootNode).put("dateEcheance", newDate);
                            }
                        }

                        // Set the updated body
                        exchange.getIn().setBody(objectMapper.writeValueAsString(rootNode));

                    } catch (Exception e) {
                        System.err.println("Error processing body: " + e.getMessage());
                        throw new RuntimeException("Error processing body", e);
                    }
                })
                .to("atlasmap:star-mapper.adm")
                .log("Mapped Output: ${body}");










        // Unmarshal JSON response
                //.to("atlasmap:star-mapper.adm") // Apply AtlasMap mapping
                //.log("Mapped Output: ${body}");
                /*.to("file://output?fileName=mapped-output.json") // Save the result to a file
                .end();*/

    }
}
