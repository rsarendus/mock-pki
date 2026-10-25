package ee.ristoseene.pki.mock.server.controller.configuration;

import ee.ristoseene.pki.mock.server.controller.ServiceController;
import ee.ristoseene.pki.mock.server.factory.configuration.AbstractApplicationContextDependantFactoryIntegrationTest;
import ee.ristoseene.pki.mock.server.factory.exception.FactoryTypeMismatchException;
import ee.ristoseene.pki.mock.server.http.HttpMethod;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationPropertiesNegativeCasesIntegrationTest extends AbstractApplicationContextDependantFactoryIntegrationTest {

    private static final String PROPERTY_KEY_FACTORY_ARGUMENT = "mock.endpoints[0].factory.argument=";
    private static final String PROPERTY_KEY_FACTORY_TYPE = "mock.endpoints[0].factory.type=";
    private static final String PROPERTY_KEY_METHOD = "mock.endpoints[0].method=";
    private static final String PROPERTY_KEY_PATH = "mock.endpoints[0].path=";

    private static final String PROPERTY_VALUE_FACTORY_ARGUMENT = "argument";
    private static final String PROPERTY_VALUE_FACTORY_TYPE = Factory.class.getCanonicalName();
    private static final String PROPERTY_VALUE_PATH = "example";

    private static final String PROPERTY_FACTORY_ARGUMENT = PROPERTY_KEY_FACTORY_ARGUMENT + PROPERTY_VALUE_FACTORY_ARGUMENT;
    private static final String PROPERTY_FACTORY_TYPE = PROPERTY_KEY_FACTORY_TYPE + PROPERTY_VALUE_FACTORY_TYPE;
    private static final String PROPERTY_PATH = PROPERTY_KEY_PATH + PROPERTY_VALUE_PATH;

    @Override
    @NullMarked
    protected ApplicationContextRunner configureApplicationContextRunner(ApplicationContextRunner applicationContextRunner) {
        return applicationContextRunner.withUserConfiguration(ServicesConfiguration.class);
    }

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void missingFactoryProperty(HttpMethod httpMethod) {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_KEY_METHOD + httpMethod.name())
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining("Field error in object 'mock' on field 'endpoints[0].factory': rejected value [null]")
                        .hasStackTraceContaining("default message [must not be null]")
                );
    }

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void missingFactoryTypeProperty(HttpMethod httpMethod) {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_FACTORY_ARGUMENT)
                .withPropertyValues(PROPERTY_KEY_METHOD + httpMethod.name())
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining("Field error in object 'mock' on field 'endpoints[0].factory.type': rejected value [null]")
                        .hasStackTraceContaining("default message [must not be null]")
                );
    }

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void invalidFactoryTypeProperty(HttpMethod httpMethod) {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_KEY_FACTORY_TYPE + "FooBar")
                .withPropertyValues(PROPERTY_KEY_METHOD + httpMethod.name())
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining("Could not find class [FooBar]")
                );
    }

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void incompatibleFactoryTypeProperty(HttpMethod httpMethod) {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_KEY_FACTORY_TYPE + IncompatibleFactory.class.getCanonicalName())
                .withPropertyValues(PROPERTY_KEY_METHOD + httpMethod.name())
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasRootCauseInstanceOf(FactoryTypeMismatchException.class)
                        .hasStackTraceContaining(
                                "The factory type '%s' is not compatible with the expected factory type '%s'",
                                IncompatibleFactory.class.getCanonicalName(),
                                ServiceController.Factory.class.getCanonicalName()
                        )
                );
    }

    @Test
    void missingMethodProperty() {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_FACTORY_TYPE)
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining("Field error in object 'mock' on field 'endpoints[0].method': rejected value [null]")
                        .hasStackTraceContaining("default message [must not be null]")
                );
    }

    @Test
    void invalidMethodProperty() {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_FACTORY_TYPE)
                .withPropertyValues(PROPERTY_KEY_METHOD + "INVALID")
                .withPropertyValues(PROPERTY_PATH)
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining(
                                "No enum constant %s.INVALID",
                                HttpMethod.class.getCanonicalName()
                        )
                );
    }

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void missingPathProperty(HttpMethod httpMethod) {
        getApplicationContextRunner()
                .withPropertyValues(PROPERTY_FACTORY_TYPE)
                .withPropertyValues(PROPERTY_KEY_METHOD + httpMethod.name())
                .run(context -> assertThat(context)
                        .hasFailed()
                        .getFailure()
                        .hasStackTraceContaining("Field error in object 'mock' on field 'endpoints[0].path': rejected value [null]")
                        .hasStackTraceContaining("default message [must not be blank]")
                );
    }

    static abstract class Factory implements ServiceController.Factory {}

    static abstract class IncompatibleFactory implements
            ee.ristoseene.pki.mock.server.factory.Factory<ServiceController> {}

}
