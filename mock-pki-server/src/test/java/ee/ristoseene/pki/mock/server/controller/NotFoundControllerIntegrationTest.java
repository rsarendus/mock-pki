package ee.ristoseene.pki.mock.server.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotFoundControllerIntegrationTest {

    @Autowired
    private RestTestClient client;

    @ParameterizedTest
    @EnumSource(HttpMethod.class)
    void requestToRandomPath_ReturnsExpectedNotFoundResponse(HttpMethod httpMethod) {
        client
                .method(transform(httpMethod))
                .uri("/some-path")
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isNotFound(),
                        spec -> spec.expectHeader().contentLength(0),
                        spec -> spec.expectBody().isEmpty(),
                        spec -> assertThat(spec.returnResult().getResponseHeaders().headerNames())
                                .usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                                .containsExactlyInAnyOrder(HttpHeaders.CONTENT_LENGTH, HttpHeaders.DATE)
                );
    }

    private static org.springframework.http.HttpMethod transform(HttpMethod httpMethod) {
        return org.springframework.http.HttpMethod.valueOf(httpMethod.name());
    }

}
