package ee.ristoseene.pki.mock.server.controller;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface ServiceController {

    void process(ControllerInput input, ControllerOutput.Provider output);

    @NullMarked
    @FunctionalInterface
    interface Factory extends ee.ristoseene.pki.mock.server.factory.Factory<ServiceController> {

        @Override
        ServiceController create(String argument);

    }

}
