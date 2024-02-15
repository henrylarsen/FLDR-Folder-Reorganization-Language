package scope;

import libs.ProgramScope;
import libs.SortableFile;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileScopeTest {
    String dataLocation = "Group9Project1/test/scope/test_data";
    Path pdfPath;

    @BeforeEach
    public void setUp() {
        pdfPath = Paths.get(dataLocation + "/Graduation.pdf").toAbsolutePath();
    }
    @Test
    public void testFileScope() {
        SortableFile file = new SortableFile(pdfPath, new ProgramScope());
        ProgramScope scope = file.getScope();
        assertTrue(scope.hasDefinition("FILE_NAME"));
        assertTrue(scope.hasDefinition("FILE_SIZE"));
        assertTrue(scope.hasDefinition("FILE_DATE"));
        assertTrue(scope.hasDefinition("FILE_TYPE"));

        String name = scope.getDefinitionValue("FILE_NAME").coerceToString();
        long size = scope.getDefinitionValue("FILE_SIZE").coerceToLong();
        String type = scope.getDefinitionValue("FILE_TYPE").coerceToString();

        String date = scope.getDefinitionValue("FILE_DATE").coerceToString();
        long day = scope.getDefinitionValue("DATE_DAY").coerceToLong();
        long month = scope.getDefinitionValue("DATE_MONTH").coerceToLong();
        long year = scope.getDefinitionValue("DATE_YEAR").coerceToLong();

        assertEquals(name, "Graduation.pdf");
        assertTrue(size > 0);
        assertEquals(type, "pdf");

        assertEquals(date, "2023-09-06T18:13:20.6391252Z");
        assertEquals(day, 6);
        assertEquals(month, 9);
        assertEquals(year, 2023);
    }
}
