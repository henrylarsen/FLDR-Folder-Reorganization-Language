# How to use FLDR

1. Open the project in IntelliJ, ensure you have ANTLR v4 plugin installed
2. In src/parser directory there are two g4 files named DSLLexer.g4 and DSLParser.g4, right click these files and click "Generate ANTLR Recognizer"
3. You should now see a new directory in the main project directory called "gen". Right click this directory and mark as "Generated Sources Root"
4. Now run src/ui/Main.java.
5. Open myscript.fldr in the user interface. 
6. Change the RESTRUCTURE statement to \[checkout dir\]/test/test_data
7. Preview, Run, and modify the script however you want for testing