package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.exception.NoSuchFactoryContextEntryException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
@FunctionalInterface
public interface FactoryContext {

    default <T> T get(Class<T> type) {
        return getTypedEntry(type).orElseThrow(() -> new NoSuchFactoryContextEntryException(String.format(
                "Factory context entry of type '%s' not found",
                type.getCanonicalName()
        )));
    }

    default <T> @Nullable T getIfPresent(Class<T> type) {
        return getTypedEntry(type).orElse(null);
    }

    default <T> T getOrDefault(Class<T> type, T defaultValue) {
        return getTypedEntry(type).orElse(defaultValue);
    }

    @Nullable Object getRawEntry(Object key);

    private <T> Optional<T> getTypedEntry(Class<T> type) {
        return Optional
                .ofNullable(getRawEntry(type))
                .filter(type::isInstance)
                .map(type::cast);
    }

}
