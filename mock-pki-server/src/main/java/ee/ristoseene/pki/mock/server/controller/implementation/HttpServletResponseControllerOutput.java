package ee.ristoseene.pki.mock.server.controller.implementation;

import ee.ristoseene.pki.mock.server.controller.ControllerOutput;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;

@NullMarked
@RequiredArgsConstructor
class HttpServletResponseControllerOutput implements ControllerOutput, ControllerOutput.Provider {

    private final @NonNull HttpServletResponse httpServletResponse;

    @Override
    public ControllerOutput withHttpStatus(int httpStatus) {
        httpServletResponse.setStatus(httpStatus);
        return this;
    }

    @Override
    public OutputStream openOutputStream(@Nullable Long outputLength) throws IOException {
        if (outputLength != null) {
            httpServletResponse.setContentLengthLong(outputLength);
        }
        return httpServletResponse.getOutputStream();
    }

}
