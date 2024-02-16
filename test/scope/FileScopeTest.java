package scope;

import libs.ProgramScope;
import libs.SortableFile;
import org.junit.jupiter.api.Test;
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

        long date = scope.getDefinitionValue("FILE_DATE").coerceToLong();
        long day = scope.getDefinitionValue("FILE_DAY").coerceToLong();
        long month = scope.getDefinitionValue("FILE_MONTH").coerceToLong();
        long year = scope.getDefinitionValue("FILE_YEAR").coerceToLong();

        assertEquals(name, "Graduation.pdf");
        assertTrue(size > 0);
        assertEquals(type, "pdf");

        assertEquals(date, 20240215);
        assertEquals(day, 15);
        assertEquals(month, 2);
        assertEquals(year, 2024);
    }
}
