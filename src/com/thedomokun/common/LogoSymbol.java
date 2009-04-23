package com.thedomokun.common;

public class LogoSymbol {
    public static final String getSymbol(int s) {
        switch(s) {
        case(LogoSym.FR):
            return "FR";
        case(LogoSym.PLUS):
            return "+";
        case(LogoSym.MULT):
            return "*";
        case(LogoSym.DIV):
            return "/";
        case(LogoSym.FL):
            return "FL";
        case(LogoSym.END):
            return "END";
        case(LogoSym.IDENTIFIER):
            return "IDENTIFIER";
        case(LogoSym.LBRACKET):
            return "[";
        case(LogoSym.RBRACKET):
            return "]";
        case(LogoSym.BACKWARD):
            return "BK";
        case(LogoSym.RIGHT):
            return "RT";
        case(LogoSym.FORWARD):
            return "FD";
        case(LogoSym.LEFT):
            return "LT";
        case(LogoSym.EOF):
            return "EOF";
        case(LogoSym.NUMBER):
            return "NUMBER";
        case(LogoSym.MINUS):
            return "-";
        case(LogoSym.BR):
            return "BR";
        case(LogoSym.error):
        case(LogoSym.INTEGER):
            return "INTEGER";
        case(LogoSym.BL):
            return "BL";
        case(LogoSym.TO):
            return "TO";
        case(LogoSym.REPEAT):
            return "REPEAT";
        case(LogoSym.DOUBLE):
            return "DOUBLE";
        case(LogoSym.MAKE):
            return "MAKE";
        case(LogoSym.QUOTE):
            return "QUOTE";
        case(LogoSym.IF):
            return "IF";
        case(LogoSym.IFELSE):
            return "IFELSE";
        case(LogoSym.PRINT):
            return "PRINT";
        case(LogoSym.SUM):
            return "SUM";
        case(LogoSym.PRODUCT):
            return "PRODUCT";
        case(LogoSym.DIFFERENCE):
            return "DIFFERENCE";
        case(LogoSym.QUOTIENT):
            return "QUOTIENT";

        case(LogoSym.LEQ):
            return "<=";
        case(LogoSym.LT):
            return "<";
        case(LogoSym.GEQ):
            return ">=";
        case(LogoSym.GT):
            return ">";
        case(LogoSym.EQ):
            return "=";

        case(LogoSym.OUTPUT):
            return "OUTPUT";
        case(LogoSym.FIRST):
            return "FIRST";
        case(LogoSym.LAST):
            return "LAST";
        case(LogoSym.BUTFIRST):
            return "BUTFIRST";
        case(LogoSym.BUTLAST):
            return "BUTLAST";

        case(LogoSym.SENTENCE):
            return "SENTENCE";
        case(LogoSym.LIST):
            return "LIST";
        case(LogoSym.ITEM):
            return "ITEM";

        case(LogoSym.EQUALP):
            return "EQUALP";
        case(LogoSym.LESSP):
            return "LESSP";
        case(LogoSym.GREATERP):
            return "GREATERP";
        }
        return "";
    }
}
