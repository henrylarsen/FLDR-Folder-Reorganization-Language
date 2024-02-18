parser grammar DSLParser;
options { tokenVocab=DSLLexer; }

// Grammar
program: start_path (condition)* (folders)+ EOF;
start_path: START PATH;
condition: CONDITION_START condition_decl COLON condition_body;
condition_decl: TEXT CONDITION_PAR_START (condition_params)? (CONDITION_PAR_END|NO_PARAM_PAR_END);
condition_params: TEXT (PARAM_SPLIT TEXT)*;

folders: (folder | for_loop);
folder: FOLDER_START (string|var) (contains)? (subfolders)?;

contains: CONTAINS_START COLON condition_body;
subfolders: HAS_SUBFOLDERS SUB_START (folders)+ ITER_END;
for_loop: FOR_EACH TEXT IN list folder;

list: ITER_START list_contents ITER_END;
list_contents: input (PARAM_SPLIT input)*;

condition_body: ((GROUP_PAR_START)? boolean (CONDITION_PAR_END?) (junction condition_body)*) | OTHER;

junction: AND | OR;
boolean: (NOT)? singular_check;
function: CONDITION_PAR_START (function_params)? (CONDITION_PAR_END|NO_PARAM_PAR_END);
comparison: operator input;

singular_check: (TEXT function) | (input (comparison | one_of));

one_of: ONEOF list;

function_params: input (PARAM_SPLIT input)*;
input: string | var | INT | size;
operator: COMP_G | COMP_L | COMP_E | INCLUDES | IS | COMP_GE | COMP_LE;

string: STRING_START string_body STRING_END;
string_body: (STRING_TEXT | string_var)*;
var: VAR_START VAR_TEXT VAR_END;
size: SIZE_B | SIZE_KB | SIZE_MB | SIZE_GB;

string_var: STRING_VAR_START STRING_TEXT STRING_VAR_END;
