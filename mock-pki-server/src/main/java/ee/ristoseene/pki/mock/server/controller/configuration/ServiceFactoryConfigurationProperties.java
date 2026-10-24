package ee.ristoseene.pki.mock.server.controller.configuration;

import ee.ristoseene.pki.mock.server.controller.ServiceController;
import ee.ristoseene.pki.mock.server.factory.FactoryUtil;
import ee.ristoseene.pki.mock.server.factory.TypeCheckingFactoryUtil;
import ee.ristoseene.pki.mock.server.factory.configuration.FactoryConfigurationProperties;
import jakarta.validation.constraints.NotNull;

record ServiceFactoryConfigurationProperties(
        @NotNull String argument,
        @NotNull Class<? extends ServiceController.Factory> type
) implements
        FactoryConfigurationProperties<ServiceController.Factory, ServiceController>
{
    public static final FactoryUtil<ServiceController.Factory, ServiceController> FACTORY_UTIL = TypeCheckingFactoryUtil
            .<ServiceController.Factory, ServiceController>builder()
            .expectedFactoryType(ServiceController.Factory.class)
            .expectedReturnType(ServiceController.class)
            .build();

    ServiceFactoryConfigurationProperties {
        if (argument == null) argument = "";
    }
}
