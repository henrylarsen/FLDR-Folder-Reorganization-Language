package ui;

import javax.swing.*;
import java.io.IOException;

public class Main {
    static String example_input = """
                RESTRUCTURE /Users/harrysadleir/Documents/school/CPSC410/Group9Project1/test/evaluator/testing_data
                CONDITION cool_photos(min_date): {FILE_TYPE} IS "png" AND {FILE_YEAR} > {min_date} AND {NAME} INCLUDES "cool"

                FOLDER "root_folder"
                    CONTAINS: {FILE_YEAR} > 1990
                    HAS SUBFOLDERS [
                        FOREACH file_type IN ["pdf", "png", "jpg"]
                            FOLDER "2024_{file_type}"
                                CONTAINS: cool_photos(2024) AND {FILE_TYPE} IS {file_type}
                                ]
                """;

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(MainFrame::new);
    }

}
