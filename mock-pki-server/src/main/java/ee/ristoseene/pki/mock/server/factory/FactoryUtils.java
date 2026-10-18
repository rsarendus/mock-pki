package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.factory.configuration.InstantiableConfigurationProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FactoryUtils {

    public static <T> T create(InstantiableConfigurationProperties<T> properties) {
        return newInstanceOf(
                getConstructorOf(properties.type(), String.class),
                properties.argument()
        );
    }

    public static <T> T create(FactoryConfigurationProperties<T> properties, ApplicationContext applicationContext) {
        Function<String, T> factory;
        try {
            factory = newInstanceOf(
                    properties.type().getConstructor(ApplicationContext.class),
                    applicationContext
            );
        } catch (NoSuchMethodException e1) {
            try {
                factory = newInstanceOf(properties.type().getConstructor());
            } catch (NoSuchMethodException e2) {
                IllegalStateException exception = new IllegalStateException(String.format(
                        "The type '%s' does not have public constructors with either parameter types: [%s] nor []",
                        properties.type(),
                        ApplicationContext.class.getCanonicalName()
                ));
                exception.addSuppressed(e1);
                exception.addSuppressed(e2);
                throw exception;
            }
        }
        return factory.apply(properties.argument());
    }

    public static <T> Constructor<T> getConstructorOf(Class<T> classType, Class<?>... parameterTypes) {
        try {
            return classType.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(String.format(
                    "The type '%s' does not have a public constructor with parameter types: [%s]",
                    classType.getCanonicalName(),
                    Stream.of(parameterTypes).map(Class::getCanonicalName)
                            .collect(Collectors.joining(", "))
            ), e);
        }
    }

    public static <T> T newInstanceOf(Constructor<T> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(String.format(
                    "The constructor '%s' of '%s' is not accessible",
                    constructor,
                    constructor.getDeclaringClass().getCanonicalName()
            ), e);
        } catch (InstantiationException e) {
            throw new IllegalStateException(String.format(
                    "The type '%s' is not instantiable",
                    constructor.getDeclaringClass().getCanonicalName()
            ), e);
        } catch (Exception e) {
            throw new IllegalStateException(String.format(
                    "Failed to create an instance of '%s' via constructor '%s'",
                    constructor.getDeclaringClass().getCanonicalName(),
                    constructor
            ), e);
        }
    }

}
