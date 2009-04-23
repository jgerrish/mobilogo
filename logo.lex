package com.thedomokun.common;

import java_cup.runtime.*;
import java.io.IOException;

import com.thedomokun.common.LogoSym;

%%

%class LogoLex

%unicode
%line
%column

%ignorecase

// %public
%final
// %abstract

%cupsym LogoSym
// Standalone instead of %cup uses int for return type instead of Yytoken
//%standalone
//%cup
//%cupdebug

%init{
	// TODO: code that goes to constructor
%init}

%{
/*
	private int new Yytoken(int type)
	{
		return new Yytoken(type, yytext());
	}

	private int new Yytoken(int type, Object value)
	{
		return new Symbol(type, yyline, yycolumn, value);
	}
*/
	private void error()
	throws IOException
	{
		throw new IOException("illegal text at line = "+yyline+", column = "+yycolumn+", text = '"+yytext()+"'");
	}
%}

PLUS        = "+"
MINUS       = "-"
MULT        = "*"
DIV         = "/"

LEQ         = "<="
LT          = "<"
GEQ         = ">="
GT          = ">"
EQ          = "="

SUM         = "SUM"
DIFFERENCE  = "DIFFERENCE"
PRODUCT     = "PRODUCT"
QUOTIENT    = "QUOTIENT"

LEFT        = "LEFT"|"LT"
RIGHT       = "RIGHT"|"RT"
FORWARD     = "FORWARD"|"FD"
BACKWARD    = "BACKWARD"|"BK"

PENUP       = "PENUP"|"PU"
PENDOWN     = "PENDOWN"|"PD"

FR          = "FR"
FL          = "FL"
BR          = "BR"
BL          = "BL"

IF          = "IF"
IFELSE      = "IFELSE"
REPEAT      = "REPEAT"
TO          = "TO"
MAKE        = "MAKE"
END         = "END"
OUTPUT      = "OUTPUT"

FIRST       = "FIRST"
LAST        = "LAST"
BUTFIRST    = "BUTFIRST"
BUTLAST     = "BUTLAST"

SENTENCE    = "SENTENCE"
LIST        = "LIST"
ITEM        = "ITEM"

EQUALP      = "EQUALP"
LESSP       = "LESSP"
GREATERP    = "GREATERP"

PRINT       = "PRINT"

LBRACKET    = "["
RBRACKET    = "]"
INTEGER     = [0-9]+
DOUBLE      = [0-9]+\.[0-9]+
IDENTIFIER  = ([:jletterdigit:]|'|\.|\?) ([:jletterdigit:]|'|\.|\?)*
COLON       = ":"
QUOTE       = "\""
//NUMBER      = DOUBLE|INTEGER
EOL         = \r|\n|\r\n
WS          = {EOL}|[ \t\f]
%%

{PLUS}		{	return new Yytoken(LogoSym.PLUS ,yytext(),yyline,yychar,yychar + 1); }
{MINUS}		{	return new Yytoken(LogoSym.MINUS ,yytext(),yyline,yychar,yychar + 1); }
{MULT}		{	return new Yytoken(LogoSym.MULT ,yytext(),yyline,yychar,yychar + 1); }
{DIV}		{	return new Yytoken(LogoSym.DIV ,yytext(),yyline,yychar,yychar + 1); }

{LEQ}		{	return new Yytoken(LogoSym.LEQ ,yytext(),yyline,yychar,yychar + 2); }
{LT}		{	return new Yytoken(LogoSym.LT ,yytext(),yyline,yychar,yychar + 1); }
{GEQ}		{	return new Yytoken(LogoSym.GEQ ,yytext(),yyline,yychar,yychar + 2); }
{GT}		{	return new Yytoken(LogoSym.GT ,yytext(),yyline,yychar,yychar + 1); }
{EQ}		{	return new Yytoken(LogoSym.EQ ,yytext(),yyline,yychar,yychar + 1); }

{SUM}		{	return new Yytoken(LogoSym.SUM ,yytext(),yyline,yychar,yychar + 3); }
{PRODUCT}	{	return new Yytoken(LogoSym.PRODUCT ,yytext(),yyline,yychar,yychar + 7); }
{DIFFERENCE} {	return new Yytoken(LogoSym.DIFFERENCE ,yytext(),yyline,yychar,yychar + 10); }
{QUOTIENT}  {	return new Yytoken(LogoSym.QUOTIENT ,yytext(),yyline,yychar,yychar + 8); }

{LEFT}		{	return new Yytoken(LogoSym.LEFT ,yytext(),yyline,yychar, yychar + yytext().length()); }
{RIGHT}		{	return new Yytoken(LogoSym.RIGHT ,yytext(),yyline,yychar, yychar + yytext().length()); }
{FORWARD}   {	return new Yytoken(LogoSym.FORWARD ,yytext(),yyline,yychar, yychar + yytext().length()); }
{BACKWARD}  {	return new Yytoken(LogoSym.BACKWARD ,yytext(),yyline,yychar, yychar + yytext().length()); }

{FR}        {	return new Yytoken(LogoSym.FR ,yytext(),yyline,yychar,yychar + 2); }
{FL}        {	return new Yytoken(LogoSym.FL ,yytext(),yyline,yychar,yychar + 2); }
{BR}        {	return new Yytoken(LogoSym.BR ,yytext(),yyline,yychar,yychar + 2); }
{BL}        {	return new Yytoken(LogoSym.BL ,yytext(),yyline,yychar,yychar + 2); }

//{NUMBER}    {   return new Yytoken(LogoSym.NUMBER, new Integer(Integer.parseInt(yytext()))); }
//{INTEGER}   {   return new Yytoken(LogoSym.INTEGER, new Integer(Integer.parseInt(yytext()))); }
{INTEGER}   {   return new Yytoken(LogoSym.INTEGER, yytext(), yyline, yychar, yychar + yytext().length()); }

{LBRACKET}  {   return new Yytoken(LogoSym.LBRACKET ,yytext(),yyline,yychar,yychar + 1); }
{RBRACKET}  {   return new Yytoken(LogoSym.RBRACKET ,yytext(),yyline,yychar,yychar + 1); }

{IF}      { return new Yytoken(LogoSym.IF ,yytext(),yyline,yychar,yychar + 2); }
{IFELSE}  { return new Yytoken(LogoSym.IFELSE ,yytext(),yyline,yychar,yychar + 6); }
{REPEAT}    {   return new Yytoken(LogoSym.REPEAT ,yytext(),yyline,yychar,yychar + 6); }
{TO}        {   return new Yytoken(LogoSym.TO ,yytext(),yyline,yychar,yychar + 2); }
{MAKE}       { return new Yytoken(LogoSym.MAKE ,yytext(),yyline,yychar,yychar + 4); }
{END}        {   return new Yytoken(LogoSym.END ,yytext(),yyline,yychar,yychar + 3); }

{OUTPUT}        {   return new Yytoken(LogoSym.OUTPUT ,yytext(),yyline,yychar,yychar + 6); }
{FIRST}        {   return new Yytoken(LogoSym.FIRST ,yytext(),yyline,yychar,yychar + 5); }
{LAST}        {   return new Yytoken(LogoSym.LAST ,yytext(),yyline,yychar,yychar + 4); }
{BUTFIRST}        {   return new Yytoken(LogoSym.BUTFIRST ,yytext(),yyline,yychar,yychar + 8); }
{BUTLAST}        {   return new Yytoken(LogoSym.BUTLAST ,yytext(),yyline,yychar,yychar + 7); }

{SENTENCE}  { return new Yytoken(LogoSym.SENTENCE, yytext(), yyline, yychar, yychar + 8); }
{LIST}  { return new Yytoken(LogoSym.LIST, yytext(), yyline, yychar, yychar + 4); }
{ITEM}  { return new Yytoken(LogoSym.ITEM, yytext(), yyline, yychar, yychar + 4); }

{EQUALP} { return new Yytoken(LogoSym.EQUALP, yytext(), yyline, yychar, yychar + 6); }
{LESSP} { return new Yytoken(LogoSym.LESSP, yytext(), yyline, yychar, yychar + 5); }
{GREATERP} { return new Yytoken(LogoSym.GREATERP, yytext(), yyline, yychar, yychar + 8); }

{PRINT}   { return new Yytoken(LogoSym.PRINT ,yytext(),yyline,yychar,yychar + 5); }

//{IDENTIFIER} {  return new Yytoken(LogoSym.IDENTIFIER, new String(yytext())); }
{IDENTIFIER} {  return new Yytoken(LogoSym.IDENTIFIER, yytext(), yyline, yychar, yychar + yytext().length()); }
{COLON}      { return new Yytoken(LogoSym.COLON ,yytext(),yyline,yychar,yychar + 1); }
{QUOTE}      { return new Yytoken(LogoSym.QUOTE ,yytext(),yyline,yychar,yychar + 1); }
{WS}        { }
{EOL}       { }
.           { System.out.println("Illegal char, '" + yytext() +
                    "' line: " + yyline + ", column: " + yychar); }
