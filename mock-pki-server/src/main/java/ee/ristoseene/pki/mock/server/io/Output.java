package ee.ristoseene.pki.mock.server.io;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;

@NullMarked
@FunctionalInterface
public interface Output {

    OutputStream openOutputStream(@Nullable Long outputLength) throws IOException;

}
