package ee.ristoseene.pki.mock.server.factory.exception;

import lombok.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FactoryReturnTypeMismatchException extends FactoryException {

    public FactoryReturnTypeMismatchException(
            @NonNull String message
    ) {
        super(message);
    }

}
