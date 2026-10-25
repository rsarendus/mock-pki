package ee.ristoseene.pki.mock.server.factory.configuration;

import ee.ristoseene.pki.mock.server.factory.FactoryContext;
import jakarta.validation.Validator;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLReadFeature;

import java.util.Map;

@NullMarked
@Configuration
class FactoryConfiguration {

    private static final String YAML_MAPPER_BEAN_NAME = "factoryConfigurationArgumentYamlMapper";

    @Bean(name = YAML_MAPPER_BEAN_NAME)
    YAMLMapper factoryConfigurationArgumentYamlMapper() {
        return YAMLMapper.builder()
                .configure(YAMLReadFeature.EMPTY_DOCUMENT_AS_EMPTY_OBJECT, true)
                .build();
    }

    @Bean
    FactoryContext factoryContext(
            @Qualifier(YAML_MAPPER_BEAN_NAME) YAMLMapper yamlMapper,
            Validator validator
    ) {
        return Map.<Object, Object>of(
                YAMLMapper.class, yamlMapper,
                Validator.class, validator
        )::get;
    }

}
