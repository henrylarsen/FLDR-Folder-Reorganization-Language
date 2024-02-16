package ui;

import java.io.IOException;

import javax.swing.*;

public class Main {
    static String example_input = """
                CONDITION cool_photos(min_date): {TYPE} IS "png" AND {FILE_YEAR} > {min_date} AND {NAME} INCLUDES "cool"

                FOLDER "root_folder"
                    CONTAINS: {FILE_YEAR} = 2020
                    HAS SUBFOLDERS
                        FOREACH file_type in ["pdf", "png", "jpg"]
                            FOLDER "2024_{file_type}"
                                CONTAINS: cool_photos(2024) AND {TYPE} IS {file_type}
                """;

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(MainFrame::new);

        // TODO: When ready to run the program, call:
//        DSLRunner runner = new DSLRunner();
//        String result = runner.run(example_input);
//        System.out.println(result);
    }

}
