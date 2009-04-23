/**
 * 
 */
package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.LogoSymbol;
import com.thedomokun.common.Yytoken;

/**
 * @author josh
 *
 */
public class LogoOp extends LogoProc {
    
    public void addArgument(LogoNode node) {
        if (arguments == null)
            arguments = new Vector();
        this.arguments.addElement(node);
    }

    public LogoVal eval(LogoEnv env) {
        LogoVal val = null;

        if ((symbol == LogoSym.SUM) ||
            (symbol == LogoSym.DIFFERENCE) ||
            (symbol == LogoSym.PRODUCT) ||
            (symbol == LogoSym.QUOTIENT) ||
            (symbol == LogoSym.PLUS) ||
            (symbol == LogoSym.MINUS) ||
            (symbol == LogoSym.MULT) ||
            (symbol == LogoSym.DIV)) {
            LogoNode n1 = (LogoNode)arguments.elementAt(0);
            LogoNode n2 = (LogoNode)arguments.elementAt(1);
            LogoVal a1 = n1.eval(env);
            LogoVal a2 = n2.eval(env);
            Integer i1 = Integer.valueOf(a1.getValue());
            Integer i2 = Integer.valueOf(a2.getValue());

            switch (symbol) {
            case LogoSym.SUM:
            case LogoSym.PLUS:
                return new LogoVal(Integer.toString(i1.intValue() + i2.intValue()));
            case LogoSym.DIFFERENCE:
            case LogoSym.MINUS:
                return new LogoVal(Integer.toString(i1.intValue() - i2.intValue()));
            case LogoSym.PRODUCT:
            case LogoSym.MULT:
                return new LogoVal(Integer.toString(i1.intValue() * i2.intValue()));
            case LogoSym.QUOTIENT:
            case LogoSym.DIV:
                return new LogoVal(Integer.toString(i1.intValue() / i2.intValue()));
            }
        } else if (symbol == LogoSym.LIST) {
            // Construct new list from two arguments
            LogoList list = new LogoList();
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoNode arg2 = (LogoNode)arguments.elementAt(1);
            LogoVal val1 = arg1.eval(env);
            LogoVal val2 = arg2.eval(env);

            list.addElement(val1);
            list.addElement(val2);

            return list;
        } else if (symbol == LogoSym.SENTENCE) {
            // Construct new list from two arguments
            LogoList list = new LogoList();
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoNode arg2 = (LogoNode)arguments.elementAt(1);
            LogoVal val1 = arg1.eval(env);
            LogoVal val2 = arg2.eval(env);

            // TODO: Handle the following with polymorphism, not conditionals
            if (val1.getType() == LogoVal.LIST) {
                LogoList l = (LogoList)val1;
                for (int i = 0; i < l.size(); i++) {
                    list.addElement(l.getMember(i));
                }
            } else
                list.addElement(val1);

            if (val2.getType() == LogoVal.LIST) {
                LogoList l = (LogoList)val2;
                for (int i = 0; i < l.size(); i++) {
                    list.addElement(l.getMember(i));
                }
            } else
                list.addElement(val2);

            return list;
        } else if (symbol == LogoSym.ITEM) {
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoNode arg2 = (LogoNode)arguments.elementAt(1);
            LogoVal val1 = arg1.eval(env);
            LogoVal val2 = arg2.eval(env);

            return val2.item(Integer.valueOf(val1.getValue()).intValue());
        } else if (symbol == LogoSym.FIRST) {
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoVal val1 = arg1.eval(env);

            return val1.first();
        } else if (symbol == LogoSym.LAST) {
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoVal val1 = arg1.eval(env);

            return val1.last();
        } else if (symbol == LogoSym.BUTFIRST) {
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoVal val1 = arg1.eval(env);

            return val1.butfirst();
        } else if (symbol == LogoSym.BUTLAST) {
            LogoNode arg1 = (LogoNode)arguments.elementAt(0);
            LogoVal val1 = arg1.eval(env);

            return val1.butlast();
        }
        return val;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(LogoSymbol.getSymbol(symbol));
        buf.append(" ");
        if (arguments != null) {
            if (arguments.size() > 0)
                buf.append(arguments.elementAt(0));
            buf.append(" ");
            if (arguments.size() > 1)
                buf.append(arguments.elementAt(1));
        }
        return buf.toString();
    }
    
    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
           throws IOException, LogoParseException {
        symbol = cur_token.m_index;
        identifier = cur_token.m_text;
        if (arguments == null)
            arguments = new Vector();

        if ((cur_token.m_index == LogoSym.SUM) ||
            (cur_token.m_index == LogoSym.DIFFERENCE) ||
            (cur_token.m_index == LogoSym.PRODUCT) ||
            (cur_token.m_index == LogoSym.QUOTIENT)) {

            arguments.addElement(parse_argument(scanner, env));
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.ITEM) {
            arguments.addElement(parse_argument(scanner, env));
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.LIST) {
            arguments.addElement(parse_argument(scanner, env));
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.SENTENCE) {
            arguments.addElement(parse_argument(scanner, env));
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.FIRST) {
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.LAST) {
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.BUTFIRST) {
            arguments.addElement(parse_argument(scanner, env));
        } else if (cur_token.m_index == LogoSym.BUTLAST) {
            arguments.addElement(parse_argument(scanner, env));
        } else if ((cur_token.m_index == LogoSym.PLUS) ||
                   (cur_token.m_index == LogoSym.MINUS) ||
                   (cur_token.m_index == LogoSym.MULT) ||
                   (cur_token.m_index == LogoSym.DIV)) {
            arguments.addElement(parse_argument(scanner, env));
        }

    }
}
