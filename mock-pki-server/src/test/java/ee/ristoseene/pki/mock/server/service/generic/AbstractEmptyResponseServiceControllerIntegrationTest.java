package ee.ristoseene.pki.mock.server.service.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@SpringBootTest(
        properties = "spring.config.additional-location=classpath:configuration/empty-response-service/",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class AbstractEmptyResponseServiceControllerIntegrationTest {

    @Autowired
    private RestTestClient client;

    @ParameterizedTest
    @ValueSource(strings = {
            "/respond-200-explicitly-no-headers-1",
            "/respond-200-explicitly-no-headers-2",
            "/respond-200-explicitly-no-headers-3"
    })
    void get_WhenNoHeadersAreExplicitlyConfiguredForHttp200_ReturnsExpectedResponse(String path) {
        client
                .get()
                .uri(path)
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentLength(0),
                        spec -> spec.expectBody().isEmpty(),
                        spec -> assertThat(spec.returnResult().getResponseHeaders().headerNames())
                                .usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                                .containsExactlyInAnyOrder(
                                        HttpHeaders.CONTENT_LENGTH,
                                        HttpHeaders.DATE
                                )
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {
            // 2xx success
            200, // OK
            201, // Created
            202, // Accepted
            203, // Non-Authoritative Information
            205, // Reset Content
            206, // Partial Content
            // 3xx redirection
            300, // Multiple Choices
            301, // Moved Permanently
            302, // Found
            303, // See Other
            305, // Use Proxy
            306, // Switch Proxy
            307, // Temporary Redirect
            308, // Permanent Redirect
            // 4xx client error
            401, // Unauthorized
            402, // Payment Required
            403, // Forbidden
            404, // Not Found
            405, // Method Not Allowed
            406, // Not Acceptable
            407, // Proxy Authentication Required
            409, // Conflict
            410, // Gone
            412, // Precondition Failed
            415, // Unsupported Media Type
            416, // Range Not Satisfiable
            417, // Expectation Failed
            418, // I'm a teapot
            421, // Misdirected Request
            422, // Unprocessable Content
            425, // Too Early
            426, // Upgrade Required
            428, // Precondition Required
            429, // Too Many Requests
            431, // Request Header Fields Too Large
            451, // Unavailable For Legal Reasons
            // 5xx server error
            502, // Bad Gateway
            504, // Gateway Timeout
            505, // HTTP Version Not Supported
            506, // Variant Also Negotiates
            510, // Not Extended
            511, // Network Authentication Required
            // Custom
            999
    })
    void get_WhenImplicitContentLengthHeaderIsExpectedForGivenStatusCode_ReturnsExpectedResponse(int statusCode) {
        client
                .get()
                .uri("/respond-" + statusCode)
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isEqualTo(statusCode),
                        spec -> spec.expectHeader().contentLength(0),
                        spec -> spec.expectBody().isEmpty(),
                        spec -> assertThat(spec.returnResult().getResponseHeaders().headerNames())
                                .usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                                .containsExactlyInAnyOrder(
                                        HttpHeaders.CONTENT_LENGTH,
                                        HttpHeaders.DATE
                                )
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {
            // 2xx success
            204, // No Content
            // 3xx redirection
            304, // Not Modified
    })
    void get_WhenNoContentLengthHeaderIsExpectedForGivenStatusCode_ReturnsExpectedResponse(int statusCode) {
        client
                .get()
                .uri("/respond-" + statusCode)
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isEqualTo(statusCode),
                        spec -> spec.expectBody().isEmpty(),
                        spec -> assertThat(spec.returnResult().getResponseHeaders().headerNames())
                                .usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                                .containsExactlyInAnyOrder(HttpHeaders.DATE)
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {
            // 4xx client error
            400, // Bad Request
            408, // Request Timeout
            411, // Length Required
            413, // Content Too Large
            414, // URI Too Long
            // 5xx server error
            500, // Internal Server Error
            501, // Not Implemented
            503, // Service Unavailable
    })
    void get_WhenConnectionIsImplicitlyClosedForGivenStatusCode_ReturnsExpectedResponse(int statusCode) {
        client
                .get()
                .uri("/respond-" + statusCode)
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isEqualTo(statusCode),
                        spec -> spec.expectHeader().valueEquals(HttpHeaders.CONNECTION, "close"),
                        spec -> spec.expectHeader().contentLength(0),
                        spec -> spec.expectBody().isEmpty(),
                        spec -> assertThat(spec.returnResult().getResponseHeaders().headerNames())
                                .usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                                .containsExactlyInAnyOrder(
                                        HttpHeaders.CONNECTION,
                                        HttpHeaders.CONTENT_LENGTH,
                                        HttpHeaders.DATE
                                )
                );
    }

}
