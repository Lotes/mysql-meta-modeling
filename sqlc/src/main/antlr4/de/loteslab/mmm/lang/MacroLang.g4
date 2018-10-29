grammar MacroLang;

start:
	Expression
	| Lambda
	| Hash
	;

Hash:
	HASH (Import | Define | Each | EachEnd | If | Else | ElseIf | EndIf)
    ;

If:     'if' condition=Expression;
Else:   'else';
ElseIf: 'elseif' condition=Expression;
EndIf:  'endif';

//CREATE TABLE x ( $each(listQuery, e -> e.name + " "+e.type)) )
//                    |      |        |         |
//                    v      v        v         v
//                  Regex  Query   Lambda     Expression
Lambda:
	params=Parameters '->' expression=Expression
;

//CREATE TABLE x (id $(type.me) PRIMARY KEY)
Expression:
	'aa' //'Expression eq=('='hm|'<>') CmpExpression
;

Parameters:
	'(' ID (',' ID)* ')'
	| ID
	| '()'
;

Import:
	'import' STRING
;

Define:
	'define' FQN
;

Each:
	'each' list=FQN 'as' var=ID ('at' index=ID)? ':' NL
;

JsonValue:
	JObject
	| JArray
	| STRING
    | NUMBER
    | 'true'
    | 'false'
    | 'null'
;

EachEnd:
	'endeach'
;

JObject:
	'{'

	'}'
;

JArray:
	'['

	']'
;

FQN:
	ID ('.' ID)*
;

ID:
	[a-zA-Z_][a-zA-Z_0-9]*
;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   ;


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
	[ \t] + -> skip
	;
	
NL:
	'\r' '\n'?|'\n'
	;

END:
	NL | [$]
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

 fragment HASH: '#';