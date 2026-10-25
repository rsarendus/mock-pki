package ee.ristoseene.pki.mock.server.factory.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

public abstract class AbstractApplicationContextDependantFactoryIntegrationTest {

    @Getter(AccessLevel.PROTECTED)
    private final ApplicationContextRunner applicationContextRunner = configureApplicationContextRunner(
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(ValidationAutoConfiguration.class))
                    .withUserConfiguration(FactoryConfiguration.class)
    );

    @NullMarked
    protected ApplicationContextRunner configureApplicationContextRunner(
            ApplicationContextRunner applicationContextRunner
    ) {
        return applicationContextRunner;
    }

    @NullMarked
    protected void runInContext(InContextExecutable executable) {
        applicationContextRunner.run(context -> {
            try {
                executable.execute(context);
            } catch (Exception e) {
                Assertions.fail("In-context execution failed with an exception", e);
            }
        });
    }

    @NullMarked
    @FunctionalInterface
    protected interface InContextExecutable {
        void execute(AssertableApplicationContext context) throws Exception;
    }

}
