package ee.ristoseene.pki.mock.server.factory.exception;

import lombok.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NoSuchFactoryContextEntryException extends IllegalStateException {

    public NoSuchFactoryContextEntryException(@NonNull String message) {
        super(message);
    }

}
