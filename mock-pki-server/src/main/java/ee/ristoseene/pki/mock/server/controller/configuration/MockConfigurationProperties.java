package ee.ristoseene.pki.mock.server.controller.configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "mock")
record MockConfigurationProperties(
        @NotNull List<@NotNull @Valid ServiceConfigurationProperties> endpoints
) {
    MockConfigurationProperties {
        if (endpoints == null) endpoints = List.of();
    }
}
