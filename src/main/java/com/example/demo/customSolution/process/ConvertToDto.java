package com.example.demo.customSolution.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

@Slf4j
public class ConvertToDto implements Processor {

        @Override
        public void process(org.apache.camel.Exchange exchange) throws Exception {
            String body = exchange.getIn().getBody(String.class);
            exchange.getIn().setBody(new Dto(body));
            log.info("Converted to Dto: {}", body);
        }

        public static class Dto {
            private String body;

            public Dto(String body) {
                this.body = body;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }
        }
}
