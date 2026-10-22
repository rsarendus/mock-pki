package ee.ristoseene.pki.mock.server.factory.exception;

import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NestedConfigurationBindException extends RuntimeException {

    public NestedConfigurationBindException(
            @NonNull String message,
            @Nullable Throwable cause
    ) {
        super(message, cause);
    }

}
