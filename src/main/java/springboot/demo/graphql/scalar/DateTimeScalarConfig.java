package springboot.demo.graphql.scalar;

import graphql.language.StringValue;
import graphql.schema.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class DateTimeScalarConfig {
    @Bean
    public GraphQLScalarType dateTimeScalarBean() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("Java LocalDateTime as scalar.")
                .coercing(new Coercing<LocalDateTime, String>() {
                    @Override
                    public String serialize(@NotNull final Object dataFetcherResult) {
                        if (dataFetcherResult instanceof LocalDateTime) {
                            return ((LocalDateTime) dataFetcherResult)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        } else {
                            throw new CoercingSerializeException("Expected a LocalDateTime object.");
                        }
                    }

                    @NotNull
                    @Override
                    public LocalDateTime parseValue(@NotNull final Object input) {
                        try {
                            if (input instanceof String) {
                                return LocalDateTime.parse((String) input,
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            } else {
                                throw new CoercingParseValueException("Expected a String");
                            }
                        } catch (DateTimeParseException e) {
                            throw new CoercingParseValueException(String.format("Not a valid dateTime: '%s'.", input), e
                            );
                        }
                    }

                    @NotNull
                    @Override
                    public LocalDateTime parseLiteral(@NotNull final Object input) {
                        if (input instanceof StringValue) {
                            try {
                                return LocalDateTime.parse(((StringValue) input).getValue(),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException(e);
                            }
                        } else {
                            throw new CoercingParseLiteralException("Expected a StringValue.");
                        }
                    }
                }).build();
    }
}
