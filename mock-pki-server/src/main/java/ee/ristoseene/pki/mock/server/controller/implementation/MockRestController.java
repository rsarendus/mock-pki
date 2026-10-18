package ee.ristoseene.pki.mock.server.controller.implementation;

import ee.ristoseene.pki.mock.server.controller.HttpMethod;
import ee.ristoseene.pki.mock.server.controller.ServiceController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@NullMarked
@RestController
@RequiredArgsConstructor
class MockRestController {

    private final String PATH_TEMPLATE = "/{service}";
    private final String LOG_MESSAGE_TEMPLATE = "Processing incoming {} request from path /{}";

    private final @NonNull ServiceController notFoundServiceController;
    private final @NonNull Map<String, ServiceController> getServiceControllers;

    @GetMapping(path = PATH_TEMPLATE)
    void get(
            @PathVariable String service,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug(LOG_MESSAGE_TEMPLATE, HttpMethod.GET, service);

        getServiceControllers
                .getOrDefault(service, notFoundServiceController)
                .process(
                        new HttpServletRequestControllerInput(request),
                        new HttpServletResponseControllerOutput(response)
                );
    }

}
