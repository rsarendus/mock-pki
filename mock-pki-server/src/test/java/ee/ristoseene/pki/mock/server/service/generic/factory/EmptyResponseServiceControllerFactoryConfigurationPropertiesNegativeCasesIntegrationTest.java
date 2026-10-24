package ee.ristoseene.pki.mock.server.service.generic.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.AbstractApplicationContextDependantFactoryIntegrationTest;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryTypeMismatchException;
import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindException;
import ee.ristoseene.pki.mock.server.factory.exception.NestedConfigurationBindValidationException;
import ee.ristoseene.pki.mock.server.http.header.HeadersConfigurer;
import ee.ristoseene.pki.mock.server.http.header.configuration.ResponseHeadersConfigurerFactoryConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmptyResponseServiceControllerFactoryConfigurationPropertiesNegativeCasesIntegrationTest
        extends AbstractApplicationContextDependantFactoryIntegrationTest {

    private static final String EXCEPTION_MESSAGE = String.format(
            "Failed to parse YAML argument for '%s'",
            EmptyResponseServiceControllerFactory.class.getCanonicalName()
    );

    @ParameterizedTest
    @ValueSource(strings = {
            StringUtils.EMPTY,
            StringUtils.SPACE
    })
    void emptyArgumentString(String argument) {
        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(NestedConfigurationBindException.class)
                .hasMessage(EXCEPTION_MESSAGE)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{}",
            "httpStatus: null"
    })
    void missingHttpStatusProperty(String argument) {
        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(NestedConfigurationBindValidationException.class)
                .hasMessage(EXCEPTION_MESSAGE)
                .hasStackTraceContaining("httpStatus: must not be null")
                .extracting(NestedConfigurationBindValidationException.class::cast)
                .extracting(NestedConfigurationBindValidationException::getConstraintViolations)
                .asInstanceOf(InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 0, 99, 1000, Integer.MAX_VALUE})
    void outOfRangeHttpStatusProperty(int statusCode) {
        String argument = "httpStatus: " + statusCode;

        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(NestedConfigurationBindValidationException.class)
                .hasMessage(EXCEPTION_MESSAGE)
                .hasStackTraceContaining("httpStatus: must be in the range of [100..999]")
                .extracting(NestedConfigurationBindValidationException.class::cast)
                .extracting(NestedConfigurationBindValidationException::getConstraintViolations)
                .asInstanceOf(InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc",
            "123",
            "[]"
    })
    void invalidHttpHeadersConfigurerFactoryProperty(String factory) {
        String argument = """
                httpStatus: 200
                httpHeadersConfigurerFactory:""" + ' ' + factory;

        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(NestedConfigurationBindException.class)
                .hasMessage(EXCEPTION_MESSAGE)
                .hasStackTraceContaining(
                        ResponseHeadersConfigurerFactoryConfigurationProperties.class.getCanonicalName()
                );
    }

    @Test
    void invalidHttpHeadersConfigurerFactoryTypeProperty() {
        String argument = """
                httpStatus: 200
                httpHeadersConfigurerFactory:
                  type: FooBar
                """;

        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(NestedConfigurationBindException.class)
                .hasMessage(EXCEPTION_MESSAGE)
                .hasStackTraceContaining("ClassNotFoundException: FooBar");
    }

    @Test
    void incompatibleHttpHeadersConfigurerFactoryTypeProperty() {
        String argument = """
                httpStatus: 200
                httpHeadersConfigurerFactory:
                  type:""" + ' ' + EmptyResponseServiceControllerFactory.class.getCanonicalName();

        assertThatThrownByFactory(argument)
                .isExactlyInstanceOf(FactoryTypeMismatchException.class)
                .hasMessage(
                        "The factory type '%s' is not compatible with the expected factory type '%s'",
                        EmptyResponseServiceControllerFactory.class.getCanonicalName(),
                        HeadersConfigurer.Factory.class.getCanonicalName()
                );
    }

    private AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownByFactory(String argument) {
        AtomicReference<AbstractThrowableAssert<?, ? extends Throwable>> throwableAssert = new AtomicReference<>();
        run(factory -> throwableAssert.set(
                assertThatThrownBy(() -> factory.create(argument))
        ));
        return throwableAssert.get();
    }

    private void run(Consumer<EmptyResponseServiceControllerFactory> factoryConsumer) {
        runInContext(context -> factoryConsumer.accept(new EmptyResponseServiceControllerFactory(context)));
    }

}
