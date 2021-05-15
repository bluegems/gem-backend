package com.bluegems.server.gembackend.config;

import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
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

    @Bean
    public GraphQLScalarType Void() {
        return new GraphQLScalarType("Void", "Scalar type to return nothing", new Coercing() {
            @Override
            public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
                return null;
            }

            @Override
            public Object parseValue(Object input) throws CoercingParseValueException {
                return null;
            }

            @Override
            public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                return null;
            }
        });
    }
}
