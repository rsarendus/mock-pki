package ee.ristoseene.pki.mock.server.factory.configuration;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface InstantiableConfigurationProperties<T> {

    String argument();
    Class<T> type();

}
