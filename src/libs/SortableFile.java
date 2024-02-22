package libs;

import libs.value.LongValue;
import libs.value.StringValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class SortableFile {
    private Path path;
    private String name;
    private long size;
    private String type;
    private long date;
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
            this.size = Long.valueOf(attr.size());
            this.date = formatDate(attr.lastModifiedTime());
            this.name = String.valueOf(this.path.getFileName());
            int extensionIndex = this.name.lastIndexOf(".");
            this.type = this.name.substring(extensionIndex + 1);

        } catch (IOException e) {
            System.out.println("Could not read attributes of file: " + this.path);
        }
    }

    private long formatDate(FileTime lastModified) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(lastModified.toString());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        return Long.parseLong(dtf.format(zonedDateTime));
    }

    private void setFileScope(ProgramScope scope) {
        this.scope = scope.buildNew();
        this.scope.setGlobalDefinition("FILE_NAME", new StringValue(this.name));
        this.scope.setGlobalDefinition("FILE_SIZE", new LongValue(this.size));
        this.scope.setGlobalDefinition("FILE_TYPE", new StringValue(this.type));
        this.scope.setGlobalDefinition("FILE_PATH", new StringValue(this.path.toString()));
        this.scope.setGlobalDefinition("FILE_DATE", new LongValue(this.date));
        this.scope.setGlobalDefinition("FILE_YEAR", new LongValue(this.date / 10000));
        this.scope.setGlobalDefinition("FILE_MONTH", new LongValue(this.date / 100 % 100));
        this.scope.setGlobalDefinition("FILE_DAY", new LongValue(this.date % 100));
    }

}