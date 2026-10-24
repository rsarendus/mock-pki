package ee.ristoseene.pki.mock.server.factory;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface Factory<R> {

    R create(String argument);

}
