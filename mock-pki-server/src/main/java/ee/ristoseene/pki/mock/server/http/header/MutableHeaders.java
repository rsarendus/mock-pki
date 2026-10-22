package ee.ristoseene.pki.mock.server.http.header;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MutableHeaders extends ReadableHeaders {

    MutableHeaders setHeader(String name, String value);

}
