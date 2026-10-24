package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryReturnTypeMismatchException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryTypeMismatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class TypeCheckingFactoryUtilTest extends FactoryUtilTest {

    @Override
    protected <F extends Factory<? extends R>, R> FactoryUtil<F, R> getFactoryUtilFor(
            Class<F> factoryType,
            Class<? extends R> returnType
    ) {
        return new TypeCheckingFactoryUtil<>(factoryType, returnType);
    }

    static Stream<Class<?>> incompatibleFactoryTypes() {
        return Stream.of(
                Object.class,
                MockReturnValue.class,
                PlainIncompatibleFactory.class,
                InterfacedIncompatibleFactory.class,
                PlainAlternativeFactory.class,
                InterfacedAlternativeFactory.class
        );
    }

    @ParameterizedTest
    @MethodSource("incompatibleFactoryTypes")
    void create_WhenPropertiesDefineFactoryOfIncompatibleType_ThrowsException(Class<?> factoryType) {
        FactoryUtil<MockFactory, MockReturnValue> factoryUtil = getFactoryUtilFor(MockFactory.class, MockReturnValue.class);
        FactoryConfigurationProperties<MockFactory, MockReturnValue> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(factoryType).when(properties).type();

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryTypeMismatchException.class)
                .withMessage(
                        "The factory type '%s' is not compatible with the expected factory type '%s'",
                        factoryType.getCanonicalName(),
                        MockFactory.class.getCanonicalName()
                );
        verifyNoInteractions(applicationContext);
    }

    @Test
    void create_WhenPropertiesDefineFactoryReturningIncompatibleValue_ThrowsException() {
        FactoryUtil<ObjectFactory, Object> factoryUtil = getFactoryUtilFor(ObjectFactory.class, String.class);
        FactoryConfigurationProperties<ObjectFactory, Object> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(ObjectFactory.class).when(properties).type();

        assertThatException()
                .isThrownBy(() -> factoryUtil.create(properties, applicationContext))
                .isExactlyInstanceOf(FactoryReturnTypeMismatchException.class)
                .withMessage(
                        "The returned value of type '%s' is not compatible with the expected return type '%s'",
                        Object.class.getCanonicalName(),
                        String.class.getCanonicalName()
                );
        verifyNoInteractions(applicationContext);
    }

    record MockReturnValue() {}

    interface MockFactory extends Factory<MockReturnValue> {}

    record IncompatibleReturnValue() {}

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class PlainIncompatibleFactory implements Factory<IncompatibleReturnValue> {
        @Override
        public IncompatibleReturnValue create(String argument) {
            return new IncompatibleReturnValue();
        }
    }

    interface IncompatibleFactory extends Factory<IncompatibleReturnValue> {}

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class InterfacedIncompatibleFactory implements IncompatibleFactory {
        @Override
        public IncompatibleReturnValue create(String argument) {
            return new IncompatibleReturnValue();
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class PlainAlternativeFactory implements Factory<MockReturnValue> {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue();
        }
    }

    interface AlternativeFactory extends Factory<MockReturnValue> {}

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class InterfacedAlternativeFactory implements AlternativeFactory {
        @Override
        public MockReturnValue create(String argument) {
            return new MockReturnValue();
        }
    }

    @NullMarked
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    static class ObjectFactory implements Factory<Object> {
        @Override
        public Object create(String argument) {
            return new Object();
        }
    }

}
