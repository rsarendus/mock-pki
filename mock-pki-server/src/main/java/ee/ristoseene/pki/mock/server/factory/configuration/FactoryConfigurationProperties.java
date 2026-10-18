package ee.ristoseene.pki.mock.server.factory.configuration;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public interface FactoryConfigurationProperties<T> extends InstantiableConfigurationProperties<Function<String, T>> {
}
