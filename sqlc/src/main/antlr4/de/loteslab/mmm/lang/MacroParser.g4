parser grammar MacroParser;

options { tokenVocab=MacroLexer; }

start: HASH (importing | define);

expression: expr=conditionalExpression;

conditionalExpression
    :   condition=logicalOrExpression (QUESTION thenPart=expression COLON elsePart=conditionalExpression)?
    ;

logicalOrExpression
    : lhs=logicalOrExpression LOR rhs=logicalAndExpression #lorExpansion
    | next=logicalAndExpression                                 #lorNext
    ;

logicalAndExpression
    : lhs=logicalAndExpression LAND rhs=inclusiveOrExpression #landExpansion
    | next=inclusiveOrExpression                                   #landNext
    ;

inclusiveOrExpression
    : lhs=inclusiveOrExpression OR rhs=exclusiveOrExpression #orExpansion
    | next=exclusiveOrExpression                                  #orNext
    ;

exclusiveOrExpression
    : lhs=exclusiveOrExpression XOR rhs=andExpression #xorExpansion
    | next=andExpression                                   #xorNext
    ;

andExpression
    : lhs=andExpression AND rhs=equalityExpression #andExpansion
    | next=equalityExpression                           #andNext
    ;

equalityExpression:
    lhs=equalityExpression op=EQ rhs=relationalExpression #equalityExpansion
    | next=relationalExpression                                #equalityNext
;

relationalExpression:
    lhs=relationalExpression op=CMP rhs=shiftExpression #relationalExpansion
    | next=shiftExpression                                   #relationalNext
    ;

shiftExpression:
    lhs=shiftExpression op=SHIFT rhs=additiveExpression #shiftExpansion
    | next=additiveExpression                                #shiftNext
;
additiveExpression:
    lhs=additiveExpression op=ADD rhs=multiplicativeExpression #additiveExpansion
    | next=multiplicativeExpression                                 #additiveNext
    ;
    
multiplicativeExpression:
    lhs=multiplicativeExpression op=MUL rhs=unaryExpression #multiplicativeExpansion
    | next=unaryExpression                                       #multiplicativeNext
    ;

unaryExpression:
    op=UNARY expr=unaryExpression   #unaryExpansion
    | next=postfixExpression             #unaryNext
    ;
    
postfixExpression:
    next=primaryExpression                                                  #postfixNext
    | expr=postfixExpression LBRACKET index=expression RBRACKET        #postfixArray
    | expr=postfixExpression LPAREN arguments=expressionList? RPAREN   #postfixFunction
    | expr=postfixExpression DOT memberName=ID                         #postfixMember
    ;

primaryExpression:
    jObject                             #primaryObject
    | jArray                            #primaryArray
    | ID                                #primaryId
    | STRING                            #primaryString
    | NUMBER                            #primaryNumber
    | TRUE                              #primaryTrue
    | FALSE                             #primaryFalse
    | NULL                              #primaryNull
    | LPAREN expr=expression RPAREN     #primaryParentheses
;

expressionList: 
    args+=expression (COMMA args+=expression)*
    ;

importing:
	IMPORT fileName=STRING
;

define:
	DEFINE name=ID body=definitionBody;

definitionBody:
    ASSIGN rvalue=expression     #definitionBodyAssign
    | SHIFT_LEFT fileName=STRING #definitionBodyLoad
    ;
    
jObject: LBRACE list=pairs? RBRACE;
pairs: list+=pair (COMMA list+=pair)*;
pair: name=pairName COLON value=expression;
pairName: ID | STRING;
jArray: LBRACKET exprs=expressionList? RBRACKET;