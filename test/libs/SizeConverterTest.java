package libs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.Assert.assertEquals;

public class SizeConverterTest {
    SizeConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new SizeConverter();
    }

    @Test
    public void unitTest() {
        String b = "100 B";
        String kb = "10 KB";
        String mb = "0.5 MB";
        String gb = "2 GB";
        assertEquals(converter.convertToBytes(b), 100);
        assertEquals(converter.convertToBytes(kb), 10240);
        assertEquals(converter.convertToBytes(mb), 524288);
        assertEquals(converter.convertToBytes(gb), 2147483648L);
    }

    @Test
    public void alternateFormats() {
        String b = "100B";
        String kb = " 10     KB  ";
        String mb = "0.5 MB";
        assertEquals(converter.convertToBytes(b), 100);
        assertEquals(converter.convertToBytes(kb), 10240);
        assertEquals(converter.convertToBytes(mb), 524288);
    }
}
