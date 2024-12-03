package com.example.demo.customSolution;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@org.springframework.context.annotation.Configuration
@BindToRegistry("myApi")
public class myApi implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // API URL
        String apiUrl = "http://10.20.0.42:64/contrat_assistance/get-by-identContrat?identProcess=fe25d109-5f23-11ef-8dc7-b64d84729be6";

        // Set up headers
        exchange.getIn().setHeader("accept", "*/*");

        // Use Camel's ProducerTemplate to make the API call
        String response = exchange.getContext().createProducerTemplate().requestBodyAndHeaders(
                apiUrl, // Target URL
                null,   // Body (null for GET requests)
                exchange.getIn().getHeaders(), // Headers
                String.class // Response type
        );

        // Log and set the response as the output body
        exchange.getIn().setBody(response);
    }
}
