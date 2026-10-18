package ee.ristoseene.pki.mock.server.controller;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface ServiceController {

    void process(ControllerInput input, ControllerOutput.Provider output);

}
