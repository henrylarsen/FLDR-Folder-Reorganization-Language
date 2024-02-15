package libs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SizeConverterTest {
    @Test
    public void unitTest() {
        String b = "100B";
        String kb = "10KB";
        String mb = "0.5MB";
        String gb = "2GB";
        assertEquals(SizeConverter.convertToBytes(b), 100);
        assertEquals(SizeConverter.convertToBytes(kb), 10240);
        assertEquals(SizeConverter.convertToBytes(mb), 524288);
        assertEquals(SizeConverter.convertToBytes(gb), 2147483648L);
    }

    @Test
    public void alternateFormats() {
        String b = "100B";
        String kb = " 10KB  ";
        assertEquals(SizeConverter.convertToBytes(b), 100);
        assertEquals(SizeConverter.convertToBytes(kb), 10240);
        //assertEquals(SizeConverter.convertToBytes(mb), 524288);
    }

    @Test
    public void testException() {
        String mb = "0.5 MB";
        assertThrows(IllegalArgumentException.class, () -> SizeConverter.convertToBytes(mb));
    }
}
