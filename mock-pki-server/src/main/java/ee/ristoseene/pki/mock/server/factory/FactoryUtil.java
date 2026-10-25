package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryInstantiationException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryReturnTypeMismatchException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;

@NullMarked
public abstract class FactoryUtil<F extends Factory<? extends R>, R> {

    public final R create(
            FactoryConfigurationProperties<F, R> properties,
            FactoryContext context
    ) {
        validateFactoryTypePresence(properties.type());
        validateFactoryType(properties.type());

        final F factory = createFactory(properties.type(), context);
        validateFactoryObject(factory);

        final R returnValue = factory.create(properties.argument());
        validateReturnValuePresence(returnValue);
        validateReturnValue(returnValue);

        return returnValue;
    }

    protected abstract void validateFactoryType(Class<? extends F> factoryType);
    protected abstract void validateFactoryObject(F factory);
    protected abstract void validateReturnValue(R returnValue);

    private static <F extends Factory<? extends R>, R> F createFactory(Class<F> type, FactoryContext context) {
        try {
            return newInstanceOf(
                    type.getConstructor(FactoryContext.class),
                    context
            );
        } catch (NoSuchMethodException e1) {
            try {
                return newInstanceOf(type.getConstructor());
            } catch (NoSuchMethodException e2) {
                final FactoryException exception = new FactoryInstantiationException(String.format(
                        "The type '%s' does not have public constructors with either parameter types: [%s] nor []",
                        type.getCanonicalName(),
                        FactoryContext.class.getCanonicalName()
                ));
                exception.addSuppressed(e1);
                exception.addSuppressed(e2);
                throw exception;
            }
        }
    }

    private static <T> T newInstanceOf(Constructor<T> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (IllegalAccessException e) {
            throw new FactoryInstantiationException(String.format(
                    "The constructor '%s' of '%s' is not accessible",
                    constructor,
                    constructor.getDeclaringClass().getCanonicalName()
            ), e);
        } catch (InstantiationException e) {
            throw new FactoryInstantiationException(String.format(
                    "The type '%s' is not instantiable",
                    constructor.getDeclaringClass().getCanonicalName()
            ), e);
        } catch (Exception e) {
            throw new FactoryInstantiationException(String.format(
                    "Failed to create an instance of '%s' via constructor '%s'",
                    constructor.getDeclaringClass().getCanonicalName(),
                    constructor
            ), e);
        }
    }

    private static void validateFactoryTypePresence(@Nullable Class<?> factoryType) {
        if (factoryType == null) throw new FactoryException("Factory is null");
    }

    private static void validateReturnValuePresence(@Nullable Object returnValue) {
        if (returnValue == null) {
            throw new FactoryReturnTypeMismatchException("Factory returned null");
        }
    }

}
