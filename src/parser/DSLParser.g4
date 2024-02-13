parser grammar DSLParser;
options { tokenVocab=DSLLexer; }

// Grammar
program: start_path (condition)* (folders)+ EOF;
start_path: START PATH;
condition: CONDITION_START condition_decl COLON condition_body;
condition_decl: TEXT CONDITION_PAR_START condition_params CONDITION_PAR_END;
condition_params: TEXT (PARAM_SPLIT TEXT)*;

folders: (folder | for_loop);
folder: FOLDER_START string (contains)? (subfolders)?;

contains: CONTAINS_START COLON condition_body;
subfolders: HAS_SUBFOLDERS folders;
for_loop: FOR_EACH TEXT IN list folder;

list: ITER_START list_contents ITER_END;
list_contents: string (PARAM_SPLIT string)*;

condition_body: boolean (junction condition_body)*;

junction: AND | OR;
boolean: (NOT)? (singular_check | one_of); // Includes IS ONEOF...
function: CONDITION_PAR_START function_params CONDITION_PAR_END;
comparison: operator input;

singular_check: input (comparison | function);

blah: (input comparison)|((string|TEXT) function);

one_of: IS ONEOF list;

function_params: input (PARAM_SPLIT input)*;
input: string | var | TEXT;
operator: COMP_G | COMP_L | COMP_E | INCLUDES | IS;

string: STRING_START string_body STRING_END;
string_body: (STRING_TEXT | string_var) (string_body)*;
var: VAR_START VAR_TEXT VAR_END;

string_var: STRING_VAR_START STRING_TEXT STRING_VAR_END;
