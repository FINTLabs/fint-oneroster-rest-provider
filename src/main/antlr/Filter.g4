grammar Filter;

@header {
    package no.fint.oneroster.antlr;
}

query : attrPath op=( EQ | NE | GT | LT | GE | LE | CO ) value ;

logical : ( query ( WS LOGICAL_OPERATOR WS query )? ) ;

attrPath
   : ATTRNAME subAttr?
   ;

subAttr
   : '.' ATTRNAME
   ;

LOGICAL_OPERATOR
   : 'AND' | 'OR'
   ;

ATTRNAME
   : ALPHA ATTR_NAME_CHAR* ;

fragment ATTR_NAME_CHAR
   : '-' | '_' | ':' | DIGIT | ALPHA
   ;

fragment DIGIT
   : ('0'..'9')
   ;

fragment ALPHA
   : ( 'A'..'Z' | 'a'..'z' )
   ;

EQ : '=' ;
NE : '!=' ;
GT : '>' ;
LT : '<' ;
GE : '>=' ;
LE : '<=' ;
CO : '~' ;

value : ANY_CHAR ;

ANY_CHAR
    : SQ (.)+? SQ
    ;

WS
    : ' '
    ;

SQ
    : '\''
    ;



