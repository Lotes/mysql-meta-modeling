lexer grammar MacroLexer;

HASH: '#';
QUESTION: '?';
COLON: ':';
LOR: '||';
LAND: '&&';
OR: '|';
AND: '&';
XOR: '^';
EQ: '==' | '!=';
CMP: '>=' |'<' |'>' |'<=';
SHIFT_LEFT: '<<';
SHIFT: SHIFT_LEFT | '>>';
ADD: '+' | '-';
MUL: '*' | '/' | '%';
UNARY:  '+' | '-' | '~' | '!';
LBRACKET: '[';
RBRACKET: ']';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
DOT: '.';
TRUE: 'true';
FALSE: 'false';
NULL: 'null';
COMMA: ',';
IMPORT: 'import';
DEFINE:	'define';
ASSIGN: '=';
ID: [a-zA-Z_][a-zA-Z_0-9]*;
STRING: '"' (ESC | SAFECODEPOINT)* '"';

fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;

fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;


fragment HEX
   : [0-9a-fA-F]
   ;


fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;

BlockComment:   
	'/*' .*? '*/' -> skip
    ;

LineComment:   
	'--' ~[\r\n]* -> skip
	;

WS: 
	[ \t\r\n] + -> skip
	;
	
NUMBER
   : '-'? INT ('.' [0-9] +)? EXP?
   ;


fragment INT
   : '0' | [1-9] [0-9]*
   ;

// no leading zeros

fragment EXP
   : [Ee] [+\-]? INT
   ;