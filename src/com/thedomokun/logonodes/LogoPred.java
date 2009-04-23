package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.LogoSymbol;
import com.thedomokun.common.Yytoken;

public class LogoPred extends LogoOp {

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(LogoSymbol.getSymbol(symbol));
        buf.append(" ");
        if (arguments != null)
            for (int i = 0; i < arguments.size(); i++) {
                if (i > 0)
                    buf.append(" ");
                buf.append(arguments.elementAt(i));
            }
        return buf.toString();
    }

    public LogoVal eval(LogoEnv env) {
        LogoVal val = null;
        LogoNode n1 = (LogoNode)arguments.elementAt(0);
        LogoNode n2 = (LogoNode)arguments.elementAt(1);
        LogoVal a1 = n1.eval(env);
        LogoVal a2 = n2.eval(env);
        if (symbol == LogoSym.EQUALP) {
            Double i1 = null, i2 = null;
            try {
                i1 = Double.valueOf(a1.getValue());
                i2 = Double.valueOf(a2.getValue());
            } catch (NumberFormatException e) {
                if (a1.getValue().equals(a2.getValue()))
                    return new LogoVal("true");
                else
                    return new LogoVal("false");
            }
            if (i1.doubleValue() == i2.doubleValue())
                return new LogoVal("true");
            else
                return new LogoVal("false"); 
        } else if (symbol == LogoSym.LESSP) {
            Integer i1 = Integer.valueOf(a1.getValue());
            Integer i2 = Integer.valueOf(a2.getValue());
            if (i1.intValue() < i2.intValue())
                return new LogoVal("true");
            else
                return new LogoVal("false");

        } else if (symbol == LogoSym.GREATERP) {
            Integer i1 = Integer.valueOf(a1.getValue());
            Integer i2 = Integer.valueOf(a2.getValue());
            if (i1.intValue() > i2.intValue())
                return new LogoVal("true");
            else
                return new LogoVal("false");
        }
        
        return val;
    }

    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
           throws IOException, LogoParseException {
        symbol = cur_token.m_index;
        identifier = cur_token.m_text;

        if (arguments == null)
            arguments = new Vector();

        if ((cur_token.m_index == LogoSym.EQUALP) ||
            (cur_token.m_index == LogoSym.LESSP) ||
            (cur_token.m_index == LogoSym.GREATERP)) {
            arguments.addElement(parse_argument(scanner, env));
            arguments.addElement(parse_argument(scanner, env));
        }
    }
}
