package ee.ristoseene.pki.mock.server.factory.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

@NullMarked
public class NestedConfigurationBindValidationException extends NestedConfigurationBindException {

    @Getter
    private final Set<? extends ConstraintViolation<?>> constraintViolations;

    public NestedConfigurationBindValidationException(
            @NonNull Set<? extends ConstraintViolation<?>> violations,
            @NonNull String message,
            @Nullable Throwable cause
    ) {
        super(message, cause);
        constraintViolations = Collections.unmodifiableSet(violations);
    }

}
