/**
 * Define a grammar called Hello
 */
grammar Meta;

translationUnit: 
	classifier+
;

classifier:
	datatype | klass
;

datatype:
	'datatype' name=ID COLON nativeType=StringLiteral SEMICOLON
;

klass:
	('interface' | 'class' | 'abstract') name=ID superTypes? 
	LBRACE
		structuralFeature*
	RBRACE
;

superTypes:
	COLON lhs=ID (COMMA rhs+=ID)*
;

structuralFeature:
	type=ID name=ID multipliers=multipliers?
;

multipliers:
	'[' lower=NUMBER? '..' upper=NUMBER? ']'
;

NUMBER: [0-9]+;


LBRACE: '{';
RBRACE: '}';

COMMA: ',';
COLON: ':';
SEMICOLON: ';';

ID : [a-zA-Z_][a-zA-Z0-9_]*;             // match lower-case identifiers

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;
    
StringLiteral
    :   EncodingPrefix? '"' SCharSequence? '"'
    ;

fragment
EncodingPrefix
    :   'u8'
    |   'u'
    |   'U'
    |   'L'
    ;

fragment
SCharSequence
    :   SChar+
    ;

fragment
SChar
    :   ~["\\\r\n]
    |   EscapeSequence
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    ;
    
fragment
EscapeSequence
    :   SimpleEscapeSequence
    |   OctalEscapeSequence
    |   HexadecimalEscapeSequence
    |   UniversalCharacterName
    ;

fragment
SimpleEscapeSequence
    :   '\\' ['"?abfnrtv\\]
    ;

fragment
OctalEscapeSequence
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' OctalDigit OctalDigit OctalDigit
    ;

fragment
HexadecimalEscapeSequence
    :   '\\x' HexadecimalDigit+
    ;
   
fragment
UniversalCharacterName
    :   '\\u' HexQuad
    |   '\\U' HexQuad HexQuad
    ;

fragment
HexQuad
    :   HexadecimalDigit HexadecimalDigit HexadecimalDigit HexadecimalDigit
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
HexadecimalDigit
    :   [0-9a-fA-F]
    ;
   