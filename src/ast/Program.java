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
        this.targetDirectory = targetDirectory;
        this.macros = macros;
        this.folders = folders;
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
        for (Path path : files) {
            String finalPathStringOfFile = targetDirectory;
            String relativePath = getRelativeTargetPathOfFile(context, path);
            if (!relativePath.isEmpty()) {  // if relative path is not empty, that means folder with matching condition is found
                finalPathStringOfFile += "/" + relativePath;
            }
            finalPathStringOfFile += "/" + path.getFileName();
            map.put(path, Paths.get(finalPathStringOfFile));
        }
        return map;
    }

    // if no matching folder condition is found, return ""
    private String getRelativeTargetPathOfFile(ProgramScope context, Path file) {
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
            throw new Error("Error walking directory " + directory);
        }
    }

    public void moveFiles(Map<Path, Path> map) {
        for (Map.Entry<Path, Path> entry : map.entrySet()) {
            Path source = entry.getKey();
            Path destination = entry.getValue();
            try {
                if (Files.exists(destination) && !source.equals(destination)) {
                    int suffix = 1;
                    String fileName = destination.getFileName().toString();
                    String baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                    while (Files.exists(destination)) {
                        fileName = baseFileName + " (" + suffix + ")" + fileExtension;
                        destination = Paths.get(destination.getParent().toString(), fileName);
                        suffix++;
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
