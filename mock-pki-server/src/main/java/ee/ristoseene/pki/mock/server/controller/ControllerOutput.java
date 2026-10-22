package ee.ristoseene.pki.mock.server.controller;

import ee.ristoseene.pki.mock.server.http.header.MutableHeaders;
import ee.ristoseene.pki.mock.server.io.Output;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

@NullMarked
public interface ControllerOutput extends Output {

    MutableHeaders getHttpHeaders();

    default ControllerOutput configureHttpHeaders(Consumer<MutableHeaders> configurer) {
        configurer.accept(getHttpHeaders());
        return this;
    }

    @NullMarked
    interface Provider {
        ControllerOutput withHttpStatus(int httpStatus);
    }

}
