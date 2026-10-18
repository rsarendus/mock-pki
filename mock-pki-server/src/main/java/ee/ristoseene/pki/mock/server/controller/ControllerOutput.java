package ee.ristoseene.pki.mock.server.controller;

import ee.ristoseene.pki.mock.server.io.Output;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ControllerOutput extends Output {

    @NullMarked
    interface Provider {
        ControllerOutput withHttpStatus(int httpStatus);
    }

}
