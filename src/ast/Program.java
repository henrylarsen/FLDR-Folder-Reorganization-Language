package ast;

import ast.folder.AbstractFolder;
import libs.Node;
import libs.ProgramScope;
import libs.SortableFile;
import libs.value.MacroValue;

import java.util.List;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program extends Node{
    private final List<Macro> macros;
    private final String targetDirectory;
    private final List<AbstractFolder> folders;

    public Program(String targetDirectory, List<Macro> macros, List<AbstractFolder> folders) {
        this.targetDirectory = targetDirectory.replaceFirst("^~", System.getProperty("user.home"));
        this.macros = macros;
        this.folders = folders;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(macros, program.macros) && Objects.equals(folders, program.folders);
    }

    public Map<Path, Path> evaluate(ProgramScope context) {
        List<Path> files = getAllFiles(Paths.get(targetDirectory));
        for (Macro macro : macros) {
            context.setGlobalDefinition(macro.getName(), new MacroValue(macro));
        }
        Map<Path, Path> map = new HashMap<>();
        for (Path filePath : files) {
            String finalFilePathString = targetDirectory;
            String relativeFilePath = getRelativeTargetPathForFile(context, filePath);
            if (!relativeFilePath.isEmpty()) {  // if relative path is not empty, that means folder with matching condition is found
                finalFilePathString += "/" + relativeFilePath;
            }
            finalFilePathString += "/" + filePath.getFileName();
            map.put(filePath, Paths.get(finalFilePathString));
        }
        return map;
    }

    // if no matching folder condition is found, return ""
    private String getRelativeTargetPathForFile(ProgramScope context, Path file) {
        SortableFile sFile = new SortableFile(file, context);
        String relativePathName = "";
        for (AbstractFolder folder : folders) {
            String evaluatePath = folder.evaluate(sFile.getScope());
            if (!evaluatePath.isEmpty()) {
                relativePathName = evaluatePath;
                break;
            }
        }
        return relativePathName;
    }

    private List<Path> getAllFiles(Path directory) {
        try (Stream<Path> stream = Files.walk(directory)){
            return stream.filter(path -> {
                try {
                    return !Files.isHidden(path) && Files.isRegularFile(path);
                } catch (IOException e) {
                    throw new Error("Failed to filter hidden and irregular files in " + directory);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new Error("Error walking directory " + directory + "; does it exist?", e);
        }
    }

    public void moveFiles(Map<Path, Path> map) {
        for (Map.Entry<Path, Path> entry : map.entrySet()) {
            Path source = entry.getKey();
            Path destination = entry.getValue();
            try {
                if (Files.exists(destination) && !source.equals(destination)) {
                    int count = 1;
                    String currentName = destination.getFileName().toString();
                    String baseName = currentName.substring(0, currentName.lastIndexOf('.'));
                    String fileType = currentName.substring(currentName.lastIndexOf('.'));
                    while (Files.exists(destination)) {
                        currentName = baseName + " (" + count + ")" + fileType;
                        destination = Paths.get(destination.getParent().toString(), currentName);
                        count++;
                    }
                }
                Files.createDirectories(destination.getParent());
                Files.move(source, destination);
                System.out.println("Files moved from " + source + " to " + destination);
            } catch (IOException e) {
                System.err.println("Error moving files: " + e.getMessage());
            }
        }
    }
    @Override
    public String toString() {
        return "Program{" +
                "targetDirectory=" + targetDirectory +
                ", macros=" + macros +
                ", folders=" + folders +
                '}';
    }
}
