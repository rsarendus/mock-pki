package ee.ristoseene.pki.mock.server.http.header.factory;

import ee.ristoseene.pki.mock.server.factory.AbstractNoInputFactory;
import ee.ristoseene.pki.mock.server.http.header.MutableHeaders;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

/// A factory for creating instances of HTTP response configurer implementations that emit no HTTP headers.
/// The implementation is guaranteed to not interact with response headers in any way.
@Slf4j
@NullMarked
public class NoOpResponseHeadersConfigurerFactory extends AbstractNoInputFactory<Consumer<MutableHeaders>> {

    private static final Consumer<MutableHeaders> NO_OP_CONFIGURER = headers -> {};

    @Override
    public Consumer<MutableHeaders> apply(String argument) {
        ensureNoInput(argument);

        log.info(
                "{}: creating no-op response headers configurer",
                getClass().getCanonicalName()
        );

        return NO_OP_CONFIGURER;
    }

}
