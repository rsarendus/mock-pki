package ee.ristoseene.pki.mock.server.controller.configuration;

import ee.ristoseene.pki.mock.server.http.HttpMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ServiceConfigurationProperties(
        @NotNull @Valid ServiceFactoryConfigurationProperties factory,
        @NotNull HttpMethod method,
        @NotBlank String path
) {
}
