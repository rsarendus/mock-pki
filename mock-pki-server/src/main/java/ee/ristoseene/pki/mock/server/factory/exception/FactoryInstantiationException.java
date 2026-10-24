package ee.ristoseene.pki.mock.server.factory.exception;

import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class FactoryInstantiationException extends FactoryException {

    public FactoryInstantiationException(
            @NonNull String message
    ) {
        super(message);
    }

    public FactoryInstantiationException(
            @NonNull String message,
            @Nullable Throwable cause
    ) {
        super(message, cause);
    }

}
