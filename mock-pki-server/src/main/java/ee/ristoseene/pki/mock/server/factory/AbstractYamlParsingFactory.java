package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindException;
import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationContext;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.util.Set;
import java.util.function.Function;

@NullMarked
@RequiredArgsConstructor
public abstract class AbstractYamlParsingFactory<R> implements Function<String, R> {

    private final YAMLMapper yamlMapper;
    private final Validator validator;

    protected AbstractYamlParsingFactory(@NonNull ApplicationContext applicationContext) {
        yamlMapper = applicationContext.getBean("factoryConfigurationArgumentYamlMapper", YAMLMapper.class);
        validator = applicationContext.getBean(Validator.class);
    }

    protected final <T> T parseAndValidate(String argument, Class<T> type) {
        final T parsedResult = parse(argument, type);
        validate(parsedResult);
        return parsedResult;
    }

    private <T> T parse(String argument, Class<T> type) {
        try {
            return yamlMapper.readValue(argument, type);
        } catch (Exception e) {
            throw new NestedConfigurationBindException(getExceptionMessage(), e);
        }
    }

    private void validate(Object validatable) {
        Set<? extends ConstraintViolation<?>> violations = validator.validate(validatable);

        if (CollectionUtils.isEmpty(violations)) {
            return;
        }

        throw new NestedConfigurationBindValidationException(
                violations,
                getExceptionMessage(),
                new ConstraintViolationException(violations)
        );
    }

    private String getExceptionMessage() {
        return String.format(
                "Failed to parse YAML argument for '%s'",
                getClass().getCanonicalName()
        );
    }

}
