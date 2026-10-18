package ee.ristoseene.pki.mock.server.io;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

@NullMarked
public interface Input {

    OptionalLong getInputLength();
    InputStream openInputStream() throws IOException;

}
