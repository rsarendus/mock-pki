package ee.ristoseene.pki.mock.server.factory.diagnostics;

import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindException;
import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindValidationException;
import jakarta.validation.ConstraintViolation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import tools.jackson.databind.DatabindException;

import java.util.Set;

@NullMarked
public class NestedConfigurationBindExceptionFailureAnalyzer
        extends AbstractFailureAnalyzer<NestedConfigurationBindException> {

    @Override
    protected @Nullable FailureAnalysis analyze(Throwable rootFailure, NestedConfigurationBindException cause) {
        StringBuilder descriptionBuilder = new StringBuilder(cause.getMessage());

        if (cause instanceof NestedConfigurationBindValidationException validationCause) {
            descriptionBuilder.append(System.lineSeparator()).append(System.lineSeparator());
            describeViolations(descriptionBuilder, validationCause.getConstraintViolations());
        } else if (cause.getCause() != null) {
            descriptionBuilder.append(System.lineSeparator()).append(System.lineSeparator());
            describeSubCause(descriptionBuilder, cause.getCause());
        }

        return new FailureAnalysis(
                descriptionBuilder.toString(),
                "Update your application's configuration",
                cause
        );
    }

    private static void describeViolations(StringBuilder descriptionBuilder, Set<? extends ConstraintViolation<?>> violations) {
        descriptionBuilder.append(violations.size() > 1 ? "Causes: " : "Cause: ");

        for (ConstraintViolation<?> violation : violations) {
            descriptionBuilder.append(System.lineSeparator()).append('\t');
            descriptionBuilder.append(violation.getPropertyPath().toString());
            descriptionBuilder.append(" : ").append(violation.getMessage());
        }
    }

    private static void describeSubCause(StringBuilder descriptionBuilder, Throwable cause) {
        if (cause instanceof DatabindException databindException) {
            descriptionBuilder.append("Cause:").append(System.lineSeparator());
            descriptionBuilder.append('\t').append(databindException.getOriginalMessage());
        } else {
            descriptionBuilder.append("Caused by '");
            descriptionBuilder.append(cause.getClass().getCanonicalName());
            descriptionBuilder.append("':").append(System.lineSeparator());
            descriptionBuilder.append('\t').append(cause.getMessage());
        }
    }

}
