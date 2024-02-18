package lexer;

import org.antlr.runtime.MismatchedTokenException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import parser.DSLLexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LexerTest {


    @Test
    public void stringTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                FOLDER "test {var} 3$\\ "
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("FOLDER", "\"", "test", "{", "var", "}", "3$\\", "\"");
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void stringTest2() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONDITION is_top_level(hi) : {FILE_PATH} IS "/Users/mazenk/Downloads"
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("CONDITION", "is_top_level", "(", "hi", ")", ":", "{", "FILE_PATH",
                "}", "IS", "\"", "/Users/mazenk/Downloads", "\"");
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void groupParenConditions() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONTAINS: (condition(s)) AND (3GB > {FILE_SIZE} OR {NAME} INCLUDES "cat")
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("CONTAINS", ":", "(", "condition", "(", "s", ")", ")", "AND", "(",
                "3GB", ">", "{", "FILE_SIZE", "}", "OR", "{", "NAME", "}", "INCLUDES", "\"", "cat", "\"", ")" );
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }


    @Test
    public void condTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONDITION new_condition(param_1, param_2):
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        assertEquals(8, tokens.size());
        List<String> expected_tokens = Arrays.asList("CONDITION", "new_condition", "(", "param_1", ",", "param_2", ")", ":");
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void condSpaceTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONDITION 
                new_condition
                    (param_1) :
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("CONDITION", "new_condition", "(", "param_1", ")", ":");
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }
    @Test
    public void forEachVarTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                FOREACH course_name IN ["info300", "phil230"]
               	    FOLDER {course_name}
               			CONTAINS: {NAME} INCLUDES {course_name}
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("FOREACH", "course_name", "IN", "[", "\"", "info300",
                "\"", ",", "\"" ,"phil230", "\"", "]", "FOLDER", "{", "course_name", "}",
                "CONTAINS", ":", "{", "NAME", "}", "INCLUDES", "{", "course_name", "}");
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void forEachConsTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                FOREACH course_name IN ["info300", "phil230"]
               	    FOLDER {course_name }
               			CONTAINS: {NAME} INCLUDES "course"
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("FOREACH", "course_name", "IN", "[", "\"", "info300",
                "\"", ",", "\"" ,"phil230", "\"", "]", "FOLDER", "{", "course_name", "}",
                "CONTAINS", ":", "{", "NAME", "}", "INCLUDES", "\"", "course", "\"");
        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void largerTest() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONDITION cool_photos(min_date): {TYPE} IS "png" AND {FILE_YEAR} > {min_date} AND {NAME} INCLUDES "cool"
                
                FOLDER "root_folder"
                    CONTAINS: {FILE_YEAR} = 2020
                    HAS SUBFOLDERS [
                        FOREACH file_type IN ["pdf", "png", "jpg"]
                            FOLDER "2024_{file_type}"
                                CONTAINS: cool_photos(2024) AND {TYPE} IS {file_type}
                                ]
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("CONDITION", "cool_photos", "(", "min_date", ")", ":", "{", "TYPE",
                "}", "IS", "\"", "png", "\"", "AND", "{", "FILE_YEAR", "}", ">", "{", "min_date", "}", "AND", "{",
                "NAME", "}", "INCLUDES", "\"", "cool", "\"",

        "FOLDER", "\"","root_folder", "\"",
        "CONTAINS", ":",  "{", "FILE_YEAR", "}",  "=",  "2020",
        "HAS SUBFOLDERS", "[",
        "FOREACH", "file_type", "IN", "[", "\"", "pdf", "\"", ",", "\"", "png", "\"", ",", "\"", "jpg", "\"",
        "]",
        "FOLDER", "\"" ,"2024_", "{", "file_type", "}", "\"",
        "CONTAINS", ":", "cool_photos", "(", "2024", ")", "AND", "{", "TYPE", "}", "IS", "{", "file_type", "}", "]");

//        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }

    @Test
    public void testLists() throws MismatchedTokenException {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString("""
                CONDITION cool_photos(min_date, max_date): {TYPE} IS "png" AND {FILE_YEAR} > {min_date} AND {NAME} INCLUDES "cool"
                
                FOLDER "root_folder"
                    CONTAINS: {FILE_YEAR} = 2020
                    HAS SUBFOLDERS [
                        FOREACH file_type IN ["pdf", "png", "jpg"]
                            FOLDER "2024_{file_type}"
                                CONTAINS: cool_photos(2024) AND {TYPE} IS {file_type}
                                ]
                """));

        List<? extends Token> tokens = lexer.getAllTokens();
        // Ignore hidden tokens
        tokens.removeIf(t -> t.getChannel() != 0);
        List<String> expected_tokens = Arrays.asList("CONDITION", "cool_photos", "(", "min_date", ",", "max_date", ")",
                ":", "{", "TYPE",
                "}", "IS", "\"", "png", "\"", "AND", "{", "FILE_YEAR", "}", ">", "{", "min_date", "}", "AND", "{",
                "NAME", "}", "INCLUDES", "\"", "cool", "\"",

                "FOLDER", "\"","root_folder", "\"",
                "CONTAINS", ":",  "{", "FILE_YEAR", "}",  "=",  "2020",
                "HAS SUBFOLDERS", "[",
                "FOREACH", "file_type", "IN", "[", "\"", "pdf", "\"", ",", "\"", "png", "\"", ",", "\"", "jpg", "\"",
                "]",
                "FOLDER", "\"" ,"2024_", "{", "file_type", "}", "\"",
                "CONTAINS", ":", "cool_photos", "(", "2024", ")", "AND", "{", "TYPE", "}", "IS", "{", "file_type", "}",
                "]");

        assertEquals(expected_tokens.size(), tokens.size());
        for (int i = 0; i < expected_tokens.size(); i++) {
            assertEquals(expected_tokens.get(i), tokens.get(i).getText().trim());
        }
    }
}

