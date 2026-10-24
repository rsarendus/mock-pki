package ee.ristoseene.pki.mock.server.controller;

import ee.ristoseene.pki.mock.server.http.header.HeadersConfigurer;
import ee.ristoseene.pki.mock.server.http.header.MutableHeaders;
import ee.ristoseene.pki.mock.server.io.Output;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ControllerOutput extends Output {

    MutableHeaders getHttpHeaders();

    default ControllerOutput configureHttpHeaders(HeadersConfigurer configurer) {
        configurer.configure(getHttpHeaders());
        return this;
    }

    @NullMarked
    interface Provider {
        ControllerOutput withHttpStatus(int httpStatus);
    }

}
