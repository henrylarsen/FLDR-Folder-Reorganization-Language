lexer grammar DSLLexer;

/*
Structure of Tokenizer is completed with a few features missing:
- usage of single quotes
*/

START: 'RESTRUCTURE' WS* -> mode(PATH_MODE);

CONDITION_START: 'CONDITION' WS* -> mode(INPUT_MODE);
CONTAINS_START: 'CONTAINS' WS*;
HAS_SUBFOLDERS: 'HAS SUBFOLDERS' WS* -> mode(SUB_MODE);
FOR_EACH: 'FOREACH' WS* -> mode(INPUT_MODE);
INCLUDES: 'INCLUDES' WS* -> mode(INPUT_MODE);
FOLDER_START: 'FOLDER' WS* -> mode(INPUT_MODE);
ONEOF: 'ONEOF' *WS;
NOT: 'NOT' WS*;

CONDITION_PAR_START: '(' WS* -> mode(INPUT_MODE);
CONDITION_PAR_END: ')' WS*;
COLON: ':' WS* -> mode(INPUT_MODE);
PARAM_SPLIT: ',' WS* -> mode(INPUT_MODE);

IN: 'IN' WS*;
IS: 'IS' WS* -> mode(INPUT_MODE);
AND: 'AND' WS* -> mode(INPUT_MODE);
OR: 'OR' WS* -> mode(INPUT_MODE);

ITER_START: '[' WS* -> mode(INPUT_MODE);
ITER_END: ']' WS*;

COMP_G: '>' WS* -> mode(INPUT_MODE);
COMP_L: '<' WS* -> mode(INPUT_MODE);
COMP_E: '=' WS* -> mode(INPUT_MODE);
COMP_GE: '>=' WS* -> mode(INPUT_MODE);
COMP_LE: '<=' WS* -> mode(INPUT_MODE);

WS : [\r\n\t ] -> channel(HIDDEN);

mode SUB_MODE;
SUB_START: '[' WS* -> mode(DEFAULT_MODE);

mode INPUT_MODE;
STRING_START: '"' -> mode(STRING_MODE);
VAR_START: '{' WS* -> mode(VAR_MODE);
TEXT: [a-zA-Z_] [a-zA-Z0-9_]* WS* -> mode(DEFAULT_MODE);
INT: [0-9]+ WS* -> mode(DEFAULT_MODE);
SIZE_B: [0-9]+'B' WS* -> mode(DEFAULT_MODE);
SIZE_KB: [0-9]+'KB' WS* -> mode(DEFAULT_MODE);
SIZE_MB: [0-9]+'MB' WS* -> mode(DEFAULT_MODE);
SIZE_GB: [0-9]+'GB' WS* -> mode(DEFAULT_MODE);
GROUP_PAR_START: '(' WS*;
NO_PARAM_PAR_END: ')' WS* -> mode(DEFAULT_MODE);
OTHER: '+OTHER' WS* -> mode(DEFAULT_MODE);

mode VAR_MODE;
VAR_TEXT: [a-zA-Z0-9_]+ WS*;
VAR_END: '}' WS* -> mode(DEFAULT_MODE);

mode STRING_MODE;
STRING_TEXT: ~[{}"\r]+;
STRING_VAR_START: '{';
STRING_VAR_END: '}';
STRING_END: '"' WS* -> mode(DEFAULT_MODE);

mode PATH_MODE;
PATH: ~[[|\]\r\n]+ WS* -> mode(DEFAULT_MODE);