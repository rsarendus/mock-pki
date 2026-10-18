package ee.ristoseene.pki.mock.server.controller.implementation;

import ee.ristoseene.pki.mock.server.controller.ControllerInput;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;

@NullMarked
@RequiredArgsConstructor
class HttpServletRequestControllerInput implements ControllerInput {

    private final @NonNull HttpServletRequest httpServletRequest;

    @Override
    public OptionalLong getInputLength() {
        final long contentLength = httpServletRequest.getContentLengthLong();
        return contentLength >= 0L
                ? OptionalLong.of(contentLength)
                : OptionalLong.empty();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return httpServletRequest.getInputStream();
    }

}
