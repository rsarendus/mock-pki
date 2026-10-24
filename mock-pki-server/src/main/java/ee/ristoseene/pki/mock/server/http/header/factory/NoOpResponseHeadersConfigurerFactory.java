package ee.ristoseene.pki.mock.server.http.header.factory;

import ee.ristoseene.pki.mock.server.factory.AbstractNoInputFactory;
import ee.ristoseene.pki.mock.server.http.header.HeadersConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

/// A factory for creating instances of HTTP response configurer implementations that emit no HTTP headers.
@Slf4j
@NullMarked
public class NoOpResponseHeadersConfigurerFactory extends AbstractNoInputFactory<HeadersConfigurer> implements HeadersConfigurer.Factory {

    private static final HeadersConfigurer NO_OP_CONFIGURER = headers -> {};

    @Override
    public HeadersConfigurer create(String argument) {
        ensureNoInput(argument);

        log.info(
                "{}: creating no-op response headers configurer",
                getClass().getCanonicalName()
        );

        return NO_OP_CONFIGURER;
    }

}
