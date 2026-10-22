package ee.ristoseene.pki.mock.server.http.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Aggregates {@link Min} and {@link Max} constraints forcing the value to be in the range of {@code [100..999]}.

@Min(100)
@Max(999)
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface HttpStatusCode {

    String message() default "{ee.ristoseene.pki.mock.server.http.annotation.HttpStatusCode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
