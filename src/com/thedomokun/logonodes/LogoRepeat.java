package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoRepeat extends LogoProc {
    private LogoCmds c;

    public LogoRepeat(LogoCmds c) {
        this.c = c;
    }

    public LogoRepeat() {
    }

    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;        
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("REPEAT ");
        buf.append(arguments.elementAt(0));
        buf.append("\n[\n");
        buf.append(c.toString());
        buf.append("]");

        return buf.toString();
    }

    public LogoVal eval(LogoEnv e) {
        int n;

        if (arguments == null)
            return null;

        LogoNode node = (LogoNode)arguments.elementAt(0);
        LogoVal val = node.eval(env);
        n = Integer.valueOf(val.getValue()).intValue();

        for(int i = 0; i < n; i++) {
            c.eval(e);
        }
        
        return null;
    }
    
    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
                throws IOException, LogoParseException {
        if (LogoNode.debug)
            System.out.println("Parsing REPEAT");
        symbol = LogoSym.REPEAT;

        arguments = new Vector();
        arguments.addElement(parse_argument(scanner, env));

        Yytoken token = scanner.yylex();
        if (token.m_index != LogoSym.LBRACKET)
            throw new LogoParseException("Expected [");
        
        c = new LogoCmds();
        c.parse(scanner, env);
        token = scanner.yylex();
        if (token.m_index != LogoSym.RBRACKET)
            throw new LogoParseException("Excpected ]");
    }
}
