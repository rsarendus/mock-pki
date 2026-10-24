package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.exception.FactoryReturnTypeMismatchException;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryTypeMismatchException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

@Builder
@NullMarked
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TypeCheckingFactoryUtil<F extends Factory<? extends R>, R> extends FactoryUtil<F, R> {

    private final @NonNull Class<? extends F> expectedFactoryType;
    private final @NonNull Class<? extends R> expectedReturnType;

    @Override
    protected void validateFactoryType(Class<? extends F> factoryType) {
        if (!expectedFactoryType.isAssignableFrom(factoryType)) {
            throw new FactoryTypeMismatchException(String.format(
                    "The factory type '%s' is not compatible with the expected factory type '%s'",
                    factoryType.getCanonicalName(),
                    expectedFactoryType.getCanonicalName()
            ));
        }
    }

    @Override
    protected void validateFactoryObject(F factory) {
        if (!expectedFactoryType.isInstance(factory)) {
            throw new FactoryTypeMismatchException(String.format(
                    "The factory of type '%s' is not compatible with the expected factory type '%s'",
                    factory.getClass().getCanonicalName(),
                    expectedFactoryType.getCanonicalName()
            ));
        }
    }

    @Override
    protected void validateReturnValue(R returnValue) {
        if (!expectedReturnType.isInstance(returnValue)) {
            throw new FactoryReturnTypeMismatchException(String.format(
                    "The returned value of type '%s' is not compatible with the expected return type '%s'",
                    returnValue.getClass().getCanonicalName(),
                    expectedReturnType.getCanonicalName()
            ));
        }
    }

}
