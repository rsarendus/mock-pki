package ee.ristoseene.pki.mock.server.service.generic.factory;

import ee.ristoseene.pki.mock.server.controller.ServiceController;
import ee.ristoseene.pki.mock.server.factory.AbstractYamlParsingFactory;
import ee.ristoseene.pki.mock.server.factory.FactoryContext;
import ee.ristoseene.pki.mock.server.http.annotation.HttpStatusCode;
import ee.ristoseene.pki.mock.server.http.header.HeadersConfigurer;
import ee.ristoseene.pki.mock.server.http.header.configuration.ResponseHeadersConfigurerFactoryConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

/// A factory for creating instances of {@link ServiceController} implementations that:
///
/// * Do not interact with incoming requests.
/// * Emit responses with:
///   - Specified HTTP status code.
///   - Specified HTTP response headers.
///   - No response body.
///
/// The argument string of the factory is expected to be in JSON/YAML format, specifying the following properties:
///
/// * `httpHeadersConfigurerFactory.argument` (String) - The argument string that is passed to the factory.
///   Requirement and format depend on the factory implementation.
/// * `httpHeadersConfigurerFactory.type` (String) - The fully qualified class name of the factory implementation used to instantiate configurers emitting HTTP response headers.
///   Optional; defaults to {@link ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory}.
/// * `httpStatus` (Number) - HTTP status code of emitted responses. Must be in the range of `[100..999]`.
@Slf4j
@NullMarked
public class EmptyResponseServiceControllerFactory extends AbstractYamlParsingFactory<ServiceController> implements ServiceController.Factory {

    private final FactoryContext factoryContext;

    public EmptyResponseServiceControllerFactory(@NonNull FactoryContext factoryContext) {
        super(factoryContext);

        this.factoryContext = factoryContext;
    }

    @Override
    public ServiceController create(String argument) {
        ConfigurationProperties configurationProperties = parseAndValidate(argument, ConfigurationProperties.class);

        final int httpStatus = configurationProperties.httpStatus();

        log.info(
                "{}: creating empty response {} with HTTP response status '{}', using response headers configurer factory '{}'",
                getClass().getCanonicalName(),
                ServiceController.class.getSimpleName(),
                httpStatus,
                configurationProperties.httpHeadersConfigurerFactory().type().getCanonicalName()
        );

        final HeadersConfigurer headersConfigurer = ResponseHeadersConfigurerFactoryConfigurationProperties.FACTORY_UTIL
                .create(configurationProperties.httpHeadersConfigurerFactory(), factoryContext);

        return (input, output) -> output
                .withHttpStatus(httpStatus)
                .configureHttpHeaders(headersConfigurer);
    }

    @NullUnmarked
    record ConfigurationProperties(
            @NotNull @Valid ResponseHeadersConfigurerFactoryConfigurationProperties httpHeadersConfigurerFactory,
            @NotNull @HttpStatusCode Integer httpStatus
    ) {
        ConfigurationProperties {
            if (httpHeadersConfigurerFactory == null)
                httpHeadersConfigurerFactory = ResponseHeadersConfigurerFactoryConfigurationProperties.builder().build();
        }
    }

}
