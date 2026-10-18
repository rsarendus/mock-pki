package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.factory.configuration.InstantiableConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class FactoryUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenInstantiableConfigurationPropertiesIsValid_ReturnExpectedValue(String argument) {
        InstantiableConfigurationProperties<MockInstantiable> properties = mockInstantiableConfigurationProperties();
        doReturn(MockInstantiable.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        MockInstantiable result = FactoryUtils.create(properties);

        assertThat(result)
                .isNotNull()
                .extracting(MockInstantiable::argument)
                .isEqualTo(argument);
    }

    @Test
    void create_WhenInstantiableConfigurationPropertiesRefersToNonPublicType_ThrowsException() {
        InstantiableConfigurationProperties<NonPublicMockInstantiable> properties = mockInstantiableConfigurationProperties();
        doReturn(NonPublicMockInstantiable.class).when(properties).type();
        doReturn(StringUtils.EMPTY).when(properties).argument();

        assertThatException()
                .isThrownBy(() -> FactoryUtils.create(properties))
                .isExactlyInstanceOf(IllegalStateException.class)
                .withMessage(
                        "The type '%s' does not have a public constructor with parameter types: [%s]",
                        NonPublicMockInstantiable.class.getCanonicalName(),
                        String.class.getCanonicalName()
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenFactoryConfigurationPropertiesRefersToValidFactoryWithContextParameter_ReturnExpectedValue(String argument) {
        FactoryConfigurationProperties<MockInstantiable> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(MockFactory.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        MockInstantiable result = FactoryUtils.create(properties, applicationContext);

        assertThat(result)
                .isNotNull()
                .extracting(MockInstantiable::argument)
                .isEqualTo(argument + ":" + applicationContext);
        verifyNoInteractions(applicationContext);
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenFactoryConfigurationPropertiesRefersToValidParameterlessFactory_ReturnExpectedValue(String argument) {
        FactoryConfigurationProperties<MockInstantiable> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(MockParameterlessFactory.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        MockInstantiable result = FactoryUtils.create(properties, applicationContext);

        assertThat(result)
                .isNotNull()
                .extracting(MockInstantiable::argument)
                .isEqualTo(argument);
        verifyNoInteractions(applicationContext);
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenFactoryConfigurationPropertiesRefersToClassWithNonMatchingConstructor_ThrowsException(String argument) {
        FactoryConfigurationProperties<MockInstantiable> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(MockInstantiable.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        assertThatException()
                .isThrownBy(() -> FactoryUtils.create(properties, applicationContext))
                .isExactlyInstanceOf(IllegalStateException.class)
                .withMessage(
                        "The type 'class %s$%s' does not have public constructors with either parameter types: [%s] nor []",
                        FactoryUtilsTest.class.getCanonicalName(),
                        MockInstantiable.class.getSimpleName(),
                        ApplicationContext.class.getCanonicalName()
                );
        verifyNoInteractions(applicationContext);
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void create_WhenFactoryConfigurationPropertiesRefersToClassWithNonMatchingInterface_ThrowsException(String argument) {
        FactoryConfigurationProperties<Void> properties = mockFactoryConfigurationProperties();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(NonFactory.class).when(properties).type();
        doReturn(argument).when(properties).argument();

        assertThatException()
                .isThrownBy(() -> FactoryUtils.create(properties, applicationContext))
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void getConstructorOf_WhenPublicConstructorWithGivenParameterTypesExists_Succeeds() {
        Constructor<MockInstantiable> result = FactoryUtils.getConstructorOf(MockInstantiable.class, String.class);

        assertThat(result).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            StringUtils.EMPTY,
            "java.lang.Object",
            "java.lang.String, java.lang.Integer"
    })
    void getConstructorOf_WhenConstructorParameterListDoesNotMatch_ThrowsException(String parametersString) {
        Class<?>[] parameterTypes = Stream
                .of(parametersString.split(", "))
                .filter(StringUtils::isNotBlank)
                .map(FactoryUtilsTest::resolveClass)
                .toArray(Class<?>[]::new);

        assertThatException()
                .isThrownBy(() -> FactoryUtils.getConstructorOf(MockInstantiable.class, parameterTypes))
                .isExactlyInstanceOf(IllegalStateException.class)
                .withMessage(
                        "The type '%s' does not have a public constructor with parameter types: [%s]",
                        MockInstantiable.class.getCanonicalName(),
                        parametersString
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {StringUtils.EMPTY, StringUtils.SPACE, "some text"})
    void newInstanceOf_WhenInstantiatingViaPublicConstructorWithCorrectArguments_Succeeds(String argument) throws Exception {
        Constructor<MockInstantiable> constructor = MockInstantiable.class.getDeclaredConstructor(String.class);

        MockInstantiable result = FactoryUtils.newInstanceOf(constructor, argument);

        assertThat(result)
                .isNotNull()
                .extracting(MockInstantiable::argument)
                .isEqualTo(argument);
    }

    @Test
    void newInstanceOf_WhenInstantiatedViaArgumentWithMismatchingType_ThrowsException() throws Exception {
        Constructor<MockInstantiable> constructor = MockInstantiable.class.getDeclaredConstructor(String.class);
        Object nonConformingArgument = new Object();

        assertThatException()
                .isThrownBy(() -> FactoryUtils.newInstanceOf(constructor, nonConformingArgument))
                .isExactlyInstanceOf(IllegalStateException.class)
                .withMessage(
                        "Failed to create an instance of '%s' via constructor 'public %s$%s(%s)'",
                        MockInstantiable.class.getCanonicalName(),
                        FactoryUtilsTest.class.getCanonicalName(),
                        MockInstantiable.class.getSimpleName(),
                        String.class.getCanonicalName()
                )
                .havingCause()
                .isInstanceOf(IllegalArgumentException.class)
                .withMessageContaining("argument type mismatch");
    }

    @Test
    void newInstanceOf_WhenTargetTypeIsNotInstantiable_ThrowException() throws Exception {
        Constructor<NonInstantiable> constructor = NonInstantiable.class.getDeclaredConstructor();

        assertThatException()
                .isThrownBy(() -> FactoryUtils.newInstanceOf(constructor))
                .isExactlyInstanceOf(IllegalStateException.class)
                .withMessage(
                        "The type '%s' is not instantiable",
                        NonInstantiable.class.getCanonicalName()
                );
    }

    @SuppressWarnings("unchecked")
    private static <T> InstantiableConfigurationProperties<T> mockInstantiableConfigurationProperties() {
        return (InstantiableConfigurationProperties<T>) mock(InstantiableConfigurationProperties.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> FactoryConfigurationProperties<T> mockFactoryConfigurationProperties() {
        return (FactoryConfigurationProperties<T>) mock(FactoryConfigurationProperties.class);
    }

    private static Class<?> resolveClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public record MockInstantiable(String argument) {}

    record NonPublicMockInstantiable(String argument) {}

    static abstract class NonInstantiable {}

    public record MockFactory(ApplicationContext applicationContext) implements Function<String, MockInstantiable> {
        @Override
        public MockInstantiable apply(String argument) {
            return new MockInstantiable(argument + ":" + applicationContext());
        }
    }

    public record MockParameterlessFactory() implements Function<String, MockInstantiable> {
        @Override
        public MockInstantiable apply(String argument) {
            return new MockInstantiable(argument);
        }
    }

    public record NonFactory(ApplicationContext applicationContext) {}

}
