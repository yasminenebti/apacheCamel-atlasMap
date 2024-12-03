package com.example.demo.test;

import io.atlasmap.core.DefaultAtlasContextFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import io.atlasmap.api.AtlasContextFactory;
import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasSession;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class AtlasMapper extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:java?period=100000")
                .setBody()
                .simple("resource:classpath:order.json") // Load input JSON
                .unmarshal().json() // Unmarshal JSON to a generic Java object
                .process(new AtlasMappingProcessor("../../../../resources/atlas.adm"))
                .log("Mapped output: ${body}"); // Log the mapped result
    }

    static class AtlasMappingProcessor implements Processor {
        private final String mappingFilePath;

        public AtlasMappingProcessor(String mappingFilePath) {
            this.mappingFilePath = mappingFilePath;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
            // Initialize AtlasMap components
            AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
            AtlasContext context = factory.createContext(new File(mappingFilePath));
            AtlasSession session = context.createSession();

            // Set source document
            String sourceDoc = exchange.getIn().getBody(String.class); // Source JSON as String
            session.setSourceDocument("myJsonSourceDoc", sourceDoc);

            // Process the mapping
            context.process(session);

            // Retrieve target document
            Object targetDoc = session.getTargetDocument("myXmlTargetDoc");

            // Set the mapped result in Camel Exchange
            exchange.getMessage().setBody(targetDoc);
        }
    }
}

