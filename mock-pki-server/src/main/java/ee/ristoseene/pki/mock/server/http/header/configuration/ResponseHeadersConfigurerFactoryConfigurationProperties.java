package ee.ristoseene.pki.mock.server.http.header.configuration;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.http.header.MutableHeaders;
import ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.function.Consumer;
import java.util.function.Function;

@Builder
public record ResponseHeadersConfigurerFactoryConfigurationProperties(
        @NotNull String argument,
        @NotNull Class<Function<String, Consumer<MutableHeaders>>> type
) implements
        FactoryConfigurationProperties<Consumer<MutableHeaders>>
{
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseHeadersConfigurerFactoryConfigurationProperties {
        if (argument == null) argument = "";
        if (type == null) type = (Class) NoOpResponseHeadersConfigurerFactory.class;
    }
}
