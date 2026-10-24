package ee.ristoseene.pki.mock.server.factory.exception;

import lombok.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FactoryTypeMismatchException extends FactoryException {

    public FactoryTypeMismatchException(
            @NonNull String message
    ) {
        super(message);
    }

}
