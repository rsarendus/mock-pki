package ee.ristoseene.pki.mock.server.factory.diagnostics;

import ee.ristoseene.pki.mock.server.factory.exception.FactoryException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

@NullMarked
public class FactoryExceptionFailureAnalyzer extends AbstractFailureAnalyzer<FactoryException> {

    @Override
    protected @Nullable FailureAnalysis analyze(Throwable rootFailure, FactoryException cause) {
        return new FailureAnalysis(
                cause.getMessage(),
                "Update your application's configuration",
                cause
        );
    }

}
