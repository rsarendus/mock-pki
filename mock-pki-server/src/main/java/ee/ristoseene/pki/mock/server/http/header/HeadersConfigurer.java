package ee.ristoseene.pki.mock.server.http.header;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface HeadersConfigurer {

    void configure(MutableHeaders headers);

    @NullMarked
    @FunctionalInterface
    interface Factory extends ee.ristoseene.pki.mock.server.factory.Factory<HeadersConfigurer> {

        @Override
        HeadersConfigurer create(String argument);

    }

}
