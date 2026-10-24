package ee.ristoseene.pki.mock.server.factory;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public abstract class AbstractNoInputFactory<R> implements Factory<R> {

    protected final void ensureNoInput(@Nullable String argument) {
        if (StringUtils.isBlank(argument)) {
            return;
        }

        final Logger log = LoggerFactory.getLogger(getClass());

        if (log.isDebugEnabled()) {
            log.warn(
                    "{}: unnecessary argument string provided: {}",
                    getClass().getCanonicalName(),
                    argument
            );
        } else {
            log.warn(
                    "{}: unnecessary argument string provided",
                    getClass().getCanonicalName()
            );
        }
    }

}
