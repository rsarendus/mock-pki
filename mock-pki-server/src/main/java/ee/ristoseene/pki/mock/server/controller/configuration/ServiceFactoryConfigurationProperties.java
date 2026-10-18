package ee.ristoseene.pki.mock.server.controller.configuration;

import ee.ristoseene.pki.mock.server.controller.ServiceController;
import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import jakarta.validation.constraints.NotNull;

import java.util.function.Function;

record ServiceFactoryConfigurationProperties(
        @NotNull String argument,
        @NotNull Class<Function<String, ServiceController>> type
) implements
        FactoryConfigurationProperties<ServiceController>
{
    ServiceFactoryConfigurationProperties {
        if (argument == null) argument = "";
    }
}
