package com.bluegems.server.gembackend.config;

import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public GraphQLScalarType Date() {
        return ExtendedScalars.Date;
    }

    @Bean
    public GraphQLScalarType DateTime() {
        return ExtendedScalars.DateTime;
    }

    @Bean
    public SchemaParserOptions schemaParserOptions() {
        return SchemaParserOptions.newOptions().objectMapperConfigurer((mapper, context) -> {
            mapper.registerModule(new JavaTimeModule());
        }).build();
    }
}
