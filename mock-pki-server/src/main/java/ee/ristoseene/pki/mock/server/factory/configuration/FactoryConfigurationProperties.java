package ee.ristoseene.pki.mock.server.factory.configuration;

import ee.ristoseene.pki.mock.server.factory.Factory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface FactoryConfigurationProperties<F extends Factory<? extends R>, R> {

    String argument();
    Class<? extends F> type();

}
