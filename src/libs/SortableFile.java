package libs;

import libs.value.LongValue;
import libs.value.StringValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;


public class SortableFile {
    private Path path;
    private String name;
    private int size;
    private String type;
    private String date;
    private ProgramScope scope;

    public SortableFile(Path path, ProgramScope scope) {
        this.path = path;
        setFileAttributes();
        setFileScope(scope);
    }

    public ProgramScope getScope() {
        return this.scope;
    }

    private void setFileAttributes() {
        try {
            BasicFileAttributes attr = Files.readAttributes(this.path, BasicFileAttributes.class);
            this.size = Math.toIntExact(Long.valueOf(attr.size())); // throws exception in case of overflow
            this.date = attr.lastModifiedTime().toString();

            this.name = String.valueOf(this.path.getFileName());
            int extensionIndex = this.name.lastIndexOf(".");
            this.type = this.name.substring(extensionIndex+1);

        } catch (IOException e) {
            System.out.println("Could not read attributes of file: " + this.path);
        }
    }

    private void setFileScope(ProgramScope scope) {
        this.scope = scope.buildNew();
        this.scope.setGlobalDefinition("FILE_NAME", new StringValue(this.name));
        this.scope.setGlobalDefinition("FILE_SIZE", new LongValue(this.size));
        this.scope.setGlobalDefinition("FILE_TYPE", new StringValue(this.type));
        this.scope.setGlobalDefinition("FILE_PATH", new StringValue(this.path.toString()));
        this.scope.setGlobalDefinition("FILE_DATE", new StringValue(this.date.toString()));
        this.scope.setGlobalDefinition("DATE_YEAR", stripDate(date, 0, 4));
        this.scope.setGlobalDefinition("DATE_MONTH", stripDate(date, 5, 7));
        this.scope.setGlobalDefinition("DATE_DAY", stripDate(date, 8, 10));
    }

    /* FileTime class already converts all values in toString method,
        so naive implementation works robustly */
    private LongValue stripDate(String date, int start, int end) {
        return new LongValue(Integer.parseInt(date.substring(start, end)));
    }


}
