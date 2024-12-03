package com.example.demo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Iterator;

public class JsonCleanerProcessor implements Processor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        /*String body = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(body);
        //String cleanedJson = jsonNode.toString();
        //String cleanedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        String cleanedJson = mapper.writeValueAsString(jsonNode);
        exchange.getIn().setBody(cleanedJson);*/

        String body = exchange.getIn().getBody(String.class);
        JsonNode rootNode = objectMapper.readTree(body);

        // Clean the JSON recursively
        JsonNode cleanedNode = cleanJson(rootNode);

        // Set the cleaned JSON back into the exchange
        exchange.getIn().setBody(objectMapper.writeValueAsString(cleanedNode));

    }

    private JsonNode cleanJson(JsonNode node) throws JsonProcessingException {
        if (node.isObject()) {
            // Iterate through all fields in the JSON object
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = node.get(fieldName);

                // Recursively process each field
                if (childNode.isTextual()) {
                    // Attempt to parse stringified JSON
                    String value = childNode.asText();
                    if (isValidJson(value)) {
                        ((ObjectNode) node).set(fieldName, cleanJson(objectMapper.readTree(value)));
                    }
                } else {
                    cleanJson(childNode);
                }
            }
        } else if (node.isArray()) {
            // Process each element in the array
            for (int i = 0; i < node.size(); i++) {
                JsonNode arrayElement = node.get(i);
                if (arrayElement.isTextual()) {
                    // Attempt to parse stringified JSON
                    String value = arrayElement.asText();
                    if (isValidJson(value)) {
                        ((ArrayNode) node).set(i, cleanJson(objectMapper.readTree(value)));
                    }
                } else {
                    cleanJson(arrayElement);
                }
            }
        }
        return node;
    }

    /**
     * Checks if a given string is valid JSON.
     */
    private boolean isValidJson(String value) {
        try {
            objectMapper.readTree(value);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
