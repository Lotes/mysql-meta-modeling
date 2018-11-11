parser grammar MacroParser;

options { tokenVocab=MacroLexer; }

start: HASH (importing | define);

expression: conditionalExpression;

conditionalExpression
    :   condition=logicalOrExpression (QUESTION thenPart=expression COLON elsePart=conditionalExpression)?
    ;

logicalOrExpression
    : lhs=logicalOrExpression LOR rhs=logicalAndExpression #lorExpansion
    | logicalAndExpression                                 #lorNext
    ;

logicalAndExpression
    : lhs=logicalAndExpression LAND rhs=inclusiveOrExpression #landExpansion
    | inclusiveOrExpression                                   #landNext
    ;

inclusiveOrExpression
    : lhs=inclusiveOrExpression OR rhs=exclusiveOrExpression #orExpansion
    | exclusiveOrExpression                                  #orNext
    ;

exclusiveOrExpression
    : lhs=exclusiveOrExpression XOR rhs=andExpression #xorExpansion
    | andExpression                                   #xorNext
    ;

andExpression
    : lhs=andExpression AND rhs=equalityExpression #andExpansion
    | equalityExpression                           #andNext
    ;

equalityExpression:
    lhs=equalityExpression op=EQ rhs=relationalExpression #equalityExpansion
    | relationalExpression                                #equalityNext
;

relationalExpression:
    lhs=relationalExpression op=CMP rhs=shiftExpression #relationalExpansion
    | shiftExpression                                   #relationalNext
    ;

shiftExpression:
    lhs=shiftExpression op=SHIFT rhs=additiveExpression #shiftExpansion
    | additiveExpression                                #shiftNext
;
additiveExpression:
    lhs=additiveExpression op=ADD rhs=multiplicativeExpression #additiveExpansion
    | multiplicativeExpression                                 #additiveNext
    ;
    
multiplicativeExpression:
    lhs=multiplicativeExpression op=MUL rhs=unaryExpression #multiplicativeExpansion
    | unaryExpression                                       #multiplicativeNext
    ;

unaryExpression:
    op=UNARY expr=unaryExpression   #unaryExpansion
    | postfixExpression             #unaryNext
    ;
    
postfixExpression:
    primaryExpression                                                  #postfixNext
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
    ASSIGN rvalue=expression
    | SHIFT_LEFT fileName=STRING
    ;
    
jObject: LBRACE list=pairs? RBRACE;
pairs: list+=pair (COMMA list+=pair)*;
pair: name=pairName COLON value=expression;
pairName: ID | STRING;
jArray: LBRACKET exprs=expressionList? RBRACKET;