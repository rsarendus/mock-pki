package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryInstantiationException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryReturnTypeMismatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public abstract class FactoryUtilTest {

    protected abstract <F extends Factory<? extends R>, R> FactoryUtil<F, R> getFactoryUtilFor(
            Class<F> factoryType,
            Class<? extends R> returnType
    );

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenPropertiesDefineValidFactoryWithoutContextParameter_ReturnsExceptedValue(String argument) {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(ParameterlessMockFactory.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        MockReturnValue result = factoryUtil.create(properties, applicationContext);

        assertThat(result).isNotNull().isInstanceOf(MockReturnValue.class);
        assertThat(result.argument()).isSameAs(argument);
        assertThat(result.context()).isNull();
        verifyNoInteractions(applicationContext);
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenPropertiesDefineValidFactoryWithContextParameter_ReturnsExceptedValue(String argument) {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(ParameterizedMockFactory.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        MockReturnValue result = factoryUtil.create(properties, applicationContext);

        assertThat(result).isNotNull().isInstanceOf(MockReturnValue.class);
        assertThat(result.argument()).isSameAs(argument);
        assertThat(result.context()).isSameAs(applicationContext);
        verifyNoInteractions(applicationContext);
    }

    static Stream<Class<? extends MockFactory>> withoutMatchingConstructorMockFactoryTypes() {
        return Stream.of(
                PrivateParameterlessMockFactory.class,
                PrivateParameterizedMockFactory.class,
                ProtectedParameterlessMockFactory.class,
                ProtectedParameterizedMockFactory.class,
                PackagePrivateParameterlessMockFactory.class,
                PackagePrivateParameterizedMockFactory.class,
                UnmatchingParameterMockFactory.class
        );
    }

    @ParameterizedTest
    @MethodSource("withoutMatchingConstructorMockFactoryTypes")
    void create_WhenPropertiesDefineFactoryWithoutMatchingConstructor_ThrowsException(Class<? extends MockFactory> factoryType) {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(factoryType).when(properties).type();

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryInstantiationException.class)
                .withMessage(
                        "The type '%s' does not have public constructors with either parameter types: [%s] nor []",
                        factoryType.getCanonicalName(),
                        ApplicationContext.class.getCanonicalName()
                );
        verifyNoInteractions(applicationContext);
    }

    static Stream<Class<? extends MockFactory>> uninstantiableMockFactoryTypes() {
        return Stream.of(
                AbstractParameterlessMockFactory.class,
                AbstractParameterizedMockFactory.class
        );
    }

    @ParameterizedTest
    @MethodSource("uninstantiableMockFactoryTypes")
    void create_WhenPropertiesDefineUninstantiableFactory_ThrowsException(Class<? extends MockFactory> factoryType) {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(factoryType).when(properties).type();

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryInstantiationException.class)
                .withMessage(
                        "The type '%s' is not instantiable",
                        factoryType.getCanonicalName()
                );
        verifyNoInteractions(applicationContext);
    }

    @Test
    void create_WhenPropertiesDefineNullFactory_ThrowsException() {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryException.class)
                .withMessage("Factory is null");
        verifyNoInteractions(applicationContext);
    }

    @Test
    void create_WhenPropertiesDefineFactoryThatReturnsNull_ThrowsException() {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(NullReturningMockFactory.class).when(properties).type();

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryReturnTypeMismatchException.class)
                .withMessage("Factory returned null");
        verifyNoInteractions(applicationContext);
    }

    @SuppressWarnings("unchecked")
    protected static <F extends Factory<R>, R> FactoryConfigurationProperties<F, R> mockFactoryConfigurationProperties() {
        return (FactoryConfigurationProperties<F, R>) mock(FactoryConfigurationProperties.class);
    }

    record MockReturnValue(String argument, ApplicationContext context) {}

    interface MockFactory extends Factory<MockReturnValue> {}

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class ParameterlessMockFactory implements MockFactory {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, null);
        }
    }

    @NullMarked
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    static class ParameterizedMockFactory implements MockFactory {
        private final ApplicationContext applicationContext;
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, applicationContext);
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class PrivateParameterlessMockFactory implements MockFactory {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, null);
        }
    }

    @NullMarked
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class PrivateParameterizedMockFactory implements MockFactory {
        private final ApplicationContext applicationContext;
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, applicationContext);
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class ProtectedParameterlessMockFactory implements MockFactory {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, null);
        }
    }

    @NullMarked
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    static class ProtectedParameterizedMockFactory implements MockFactory {
        private final ApplicationContext applicationContext;
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, applicationContext);
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    static class PackagePrivateParameterlessMockFactory implements MockFactory {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, null);
        }
    }

    @NullMarked
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    static class PackagePrivateParameterizedMockFactory implements MockFactory {
        private final ApplicationContext applicationContext;
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue(argument, applicationContext);
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static abstract class AbstractParameterlessMockFactory implements MockFactory {
    }

    @NullMarked
    @SuppressWarnings("unused")
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    static abstract class AbstractParameterizedMockFactory implements MockFactory {
        private final ApplicationContext applicationContext;
    }

    @NullMarked
    @SuppressWarnings("unused")
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    static abstract class UnmatchingParameterMockFactory implements MockFactory {
        private final String argument;
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class NullReturningMockFactory implements MockFactory {
        @Override
        @SuppressWarnings("all")
        public MockReturnValue create(String argument) {
            return null;
        }
    }

}
