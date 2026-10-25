package ee.ristoseene.pki.mock.server.factory;

import ee.ristoseene.pki.mock.server.factory.exception.NoSuchFactoryContextEntryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FactoryContextTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private FactoryContext factoryContext;

    @Test
    void get_WhenGetRawEntryReturnsNull_ThrowsException() {
        Class<String> key = String.class;
        doReturn(null).when(factoryContext).getRawEntry(key);

        assertThatException()
                .isThrownBy(() -> factoryContext.get(key))
                .isExactlyInstanceOf(NoSuchFactoryContextEntryException.class)
                .withMessage("Factory context entry of type 'java.lang.String' not found");
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void get_WhenGetRawEntryReturnsObjectOfIncompatibleType_ThrowsException() {
        Class<String> key = String.class;
        Object value = new Object();
        doReturn(value).when(factoryContext).getRawEntry(key);

        assertThatException()
                .isThrownBy(() -> factoryContext.get(key))
                .isExactlyInstanceOf(NoSuchFactoryContextEntryException.class)
                .withMessage("Factory context entry of type 'java.lang.String' not found");
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void get_WhenGetRawEntryReturnsObjectOfExactType_ReturnsEntry() {
        Class<String> key = String.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        String result = factoryContext.get(key);

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void get_WhenGetRawEntryReturnsObjectOfSubType_ReturnsEntry() {
        Class<CharSequence> key = CharSequence.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        CharSequence result = factoryContext.get(key);

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getIfPresent_WhenGetRawEntryReturnsNull_ReturnsNull() {
        Class<String> key = String.class;
        doReturn(null).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getIfPresent(key);

        assertThat(result).isNull();
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getIfPresent_WhenGetRawEntryReturnsObjectOfIncompatibleType_ReturnsNull() {
        Class<String> key = String.class;
        Object value = new Object();
        doReturn(value).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getIfPresent(key);

        assertThat(result).isNull();
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getIfPresent_WhenGetRawEntryReturnsObjectOfExactType_ReturnsEntry() {
        Class<String> key = String.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getIfPresent(key);

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getIfPresent_WhenGetRawEntryReturnsObjectOfSubType_ReturnsEntry() {
        Class<CharSequence> key = CharSequence.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        CharSequence result = factoryContext.getIfPresent(key);

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getIfDefault_WhenGetRawEntryReturnsNull_ReturnsDefaultValue() {
        Class<String> key = String.class;
        String defaultValue = "defaultValue";
        doReturn(null).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getOrDefault(key, defaultValue);

        assertThat(result).isSameAs(defaultValue);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getOrDefault_WhenGetRawEntryReturnsObjectOfIncompatibleType_ReturnsDefaultValue() {
        Class<String> key = String.class;
        String defaultValue = "defaultValue";
        doReturn(new Object()).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getOrDefault(key, defaultValue);

        assertThat(result).isSameAs(defaultValue);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getOrDefault_WhenGetRawEntryReturnsObjectOfExactType_ReturnsEntry() {
        Class<String> key = String.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        String result = factoryContext.getOrDefault(key, "defaultValue");

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

    @Test
    void getOrDefault_WhenGetRawEntryReturnsObjectOfSubType_ReturnsEntry() {
        Class<CharSequence> key = CharSequence.class;
        String value = "value";
        doReturn(value).when(factoryContext).getRawEntry(key);

        CharSequence result = factoryContext.getOrDefault(key, "defaultValue");

        assertThat(result).isSameAs(value);
        verify(factoryContext).getRawEntry(key);
    }

}
