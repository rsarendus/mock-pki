package ee.ristoseene.pki.mock.server.http.header.configuration;

import ee.ristoseene.pki.mock.server.factory.FactoryUtil;
import ee.ristoseene.pki.mock.server.factory.TypeCheckingFactoryUtil;
import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.http.header.HeadersConfigurer;
import ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ResponseHeadersConfigurerFactoryConfigurationProperties(
        @NotNull String argument,
        @NotNull Class<? extends HeadersConfigurer.Factory> type
) implements
        FactoryConfigurationProperties<HeadersConfigurer.Factory, HeadersConfigurer>
{
    public static final FactoryUtil<HeadersConfigurer.Factory, HeadersConfigurer> FACTORY_UTIL = TypeCheckingFactoryUtil
            .<HeadersConfigurer.Factory, HeadersConfigurer>builder()
            .expectedFactoryType(HeadersConfigurer.Factory.class)
            .expectedReturnType(HeadersConfigurer.class)
            .build();

    public ResponseHeadersConfigurerFactoryConfigurationProperties {
        if (argument == null) argument = "";
        if (type == null) type = NoOpResponseHeadersConfigurerFactory.class;
    }
}
