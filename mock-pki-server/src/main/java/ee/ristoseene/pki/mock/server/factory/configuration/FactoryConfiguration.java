package ee.ristoseene.pki.mock.server.factory.configuration;

import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLReadFeature;

@NullMarked
@Configuration
class FactoryConfiguration {

    @Bean(name = "factoryConfigurationArgumentYamlMapper")
    YAMLMapper factoryConfigurationArgumentYamlMapper() {
        return YAMLMapper.builder()
                .configure(YAMLReadFeature.EMPTY_DOCUMENT_AS_EMPTY_OBJECT, true)
                .build();
    }

}
