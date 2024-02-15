package evaluator;

import ast.Program;
import ast.condition.AbstractCondition;
import ast.condition.CatchAllCondition;
import ast.condition.NegationCondition;
import ast.condition.OneOfCondition;
import ast.condition.comparison.string.StringComparison;
import ast.condition.comparison.string.StringComparisonType;
import ast.folder.ForEachFolder;
import ast.folder.SingleFolder;
import ast.operand.ConstantOperand;
import ast.operand.TemplateOperand;
import ast.operand.VariableOperand;
import libs.ProgramScope;
import libs.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ProgramTest {
    AbstractCondition pdfComparison = new StringComparison(
            new VariableOperand("FILE_TYPE"),
            new ConstantOperand(new StringValue("pdf")),
            StringComparisonType.MATCHES);
    AbstractCondition thunderbirdFilesComparison = new StringComparison(
            new VariableOperand("FILE_NAME"),
            new ConstantOperand(new StringValue("thunderbird")),
            StringComparisonType.CONTAINS
    );
    AbstractCondition oneOfJpgPng = new OneOfCondition(
            new VariableOperand("FILE_TYPE"),
            new ArrayList<>(Arrays.asList(
                    new ConstantOperand(new StringValue("jpg")),
                    new ConstantOperand(new StringValue("png"))
            ))
    );
    SingleFolder pdfFolder = new SingleFolder(
            new ConstantOperand(new StringValue("pdfs")),
            pdfComparison,
            new ArrayList<>());
    ForEachFolder forPdfTxtFolder = new ForEachFolder(
            "ending",
            new ArrayList<>(Arrays.asList(new ConstantOperand(new StringValue("pdf")), new ConstantOperand(new StringValue("txt")))),
            new ArrayList<>(Arrays.asList(
                    new SingleFolder(
                            new TemplateOperand(List.of(new VariableOperand("ending")), "$-folder"),
                            new StringComparison(
                                    new VariableOperand("FILE_TYPE"),
                                    new VariableOperand("ending"),
                                    StringComparisonType.MATCHES
                            ),
                            new ArrayList<>()
                    )
            )));

    SingleFolder complexNestFolder = new SingleFolder(
            new ConstantOperand(new StringValue("my files")),
            new CatchAllCondition(),
            new ArrayList<>(Arrays.asList(forPdfTxtFolder))
    );

    SingleFolder thunderbirdFolder = new SingleFolder(
            new ConstantOperand(new StringValue("thunderbird")),
            thunderbirdFilesComparison,
            new ArrayList<>()
    );

    SingleFolder everythingExceptThunderbirdFolder = new SingleFolder(
            new ConstantOperand(new StringValue("not thunderbird")),
            new NegationCondition(thunderbirdFilesComparison),
            new ArrayList<>()
    );

    SingleFolder forEachImgFolder = new SingleFolder(
            new ConstantOperand(new StringValue("pictures")),
            oneOfJpgPng,
            new ArrayList<>()
    );

    String testDirectory = "test/evaluator";
    String dataLocation = testDirectory + "/testing_data";
    String targetDirectory;

    String runId;
    Program program;

    private static String generateRandomString(int length) {
        final String ALLOWED_CHARACTERS = "0123456789";

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    // expected relative directory is relative to the target directory
    // checker is what condition the file matches to be in that relative directory
    private void verifyMapping(Map<Path, Path> map, String expectedRelativeDirectory, Function<Path, Boolean> checker) {
        for (Path path : map.keySet()) {
            if (checker.apply(path)) {
                Assertions.assertEquals(map.get(path).getParent().toString(), targetDirectory + expectedRelativeDirectory);
            }
        }
    }

    @BeforeEach
    public void setUp() {
        runId = generateRandomString(4);
        targetDirectory = testDirectory + "/runs/test " + runId;
        Path sourceDir = Paths.get(dataLocation).toAbsolutePath();
        Path targetDir = Paths.get(targetDirectory).toAbsolutePath();
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            stream.forEach(source -> {
                Path target = targetDir.resolve(sourceDir.relativize(source));
                try {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.err.println("Failed to copy " + source + " to " + target);
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to copy files");
        }
    }

    @AfterEach
    public void closeDown() {
        try {
            Files.walk(Paths.get(targetDirectory))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (IOException e) {
            System.err.println("Failed to clean up directory: " + targetDirectory);
        }
    }

    @Test
    void testMovingFiles() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(pdfFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        verifyMapping(
                map,
                "/pdfs",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("pdf");
                });
        program.moveFiles(map);
    }

    @Test
    void testMovingForEachFiles() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(forPdfTxtFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
//        Assertions.assertEquals(6, map.size());
        verifyMapping(
                map,
                "/pdf-folder",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("pdf");
                });
        verifyMapping(
                map,
                "/txt-folder",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("txt");
                });
        program.moveFiles(map);
    }

    @Test
    void testMovingNestedFiles() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(complexNestFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        program.moveFiles(map);
    }

    @Test
    void testContainsNameFiles() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(thunderbirdFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        verifyMapping(
                map,
                "/thunderbird",
                path -> path.getFileName().toString().contains("thunderbird"));
        program.moveFiles(map);
    }

    @Test
    void testNegationFolder() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(everythingExceptThunderbirdFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        verifyMapping(
                map,
                "/not thunderbird",
                path -> !path.getFileName().toString().contains("thunderbird"));
        program.moveFiles(map);
    }

    @Test
    void testPhotosFolder() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(forEachImgFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        verifyMapping(
                map,
                "/pictures",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("jpg") ||
                            fileName.substring(extensionIndex+1).matches("png");
                });
        program.moveFiles(map);
    }

    @Test
    void testMultipleFolders() {
        program = new Program(targetDirectory, new ArrayList<>(), Arrays.asList(forEachImgFolder, pdfFolder));
        Map<Path, Path> map = program.evaluate(new ProgramScope());
        verifyMapping(
                map,
                "/pictures",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("jpg") ||
                            fileName.substring(extensionIndex+1).matches("png");
                });
        verifyMapping(
                map,
                "/pdfs",
                path -> {
                    String fileName = path.getFileName().toString();
                    int extensionIndex = fileName.lastIndexOf(".");
                    return fileName.substring(extensionIndex+1).matches("pdf");
                });
        program.moveFiles(map);
    }
}
