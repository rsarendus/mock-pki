package ee.ristoseene.pki.mock.server.controller.configuration;

import ee.ristoseene.pki.mock.server.controller.ServiceController;
import ee.ristoseene.pki.mock.server.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NullMarked
@Configuration
@EnableConfigurationProperties(MockConfigurationProperties.class)
class ServicesConfiguration {

    @Bean
    ServiceController notFoundServiceController() {
        final int httpStatus = HttpStatus.NOT_FOUND.value();
        return (input, output) -> output.withHttpStatus(httpStatus);
    }

    @Bean
    Map<String, ServiceController> getServiceControllers(
            @Autowired(required = false) @Nullable MockConfigurationProperties configurationProperties,
            ApplicationContext applicationContext
    ) {
        return createServiceControllers(configurationProperties, applicationContext, HttpMethod.GET);
    }

    private static Map<String, ServiceController> createServiceControllers(
            @Nullable MockConfigurationProperties configurationProperties,
            ApplicationContext applicationContext,
            HttpMethod httpMethod
    ) {
        if (configurationProperties == null || CollectionUtils.isEmpty(configurationProperties.endpoints())) {
            return Collections.emptyMap();
        }

        final Map<String, ServiceController> serviceControllers = new HashMap<>();

        for (final ServiceConfigurationProperties properties : configurationProperties.endpoints()) {
            if (properties.method() != httpMethod) {
                continue;
            } else if (serviceControllers.containsKey(properties.path())) {
                throw new IllegalStateException(String.format(
                        "Duplicate %s service controller for path '/%s'",
                        properties.method(),
                        properties.path()
                ));
            }

            log.info(
                    "Initializing {} service controller for path '/{}', using factory '{}'",
                    properties.method(),
                    properties.path(),
                    properties.factory().type().getCanonicalName()
            );

            serviceControllers.put(
                    properties.path(),
                    ServiceFactoryConfigurationProperties.FACTORY_UTIL
                            .create(properties.factory(), applicationContext)
            );
        }

        return Collections.unmodifiableMap(serviceControllers);
    }

}
