package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoFunc extends LogoProc {
    String identifier;
    LogoCmds body = null;
    public Vector parameters = null;

    public LogoFunc(String i, LogoCmds l) {
        identifier = i;
        body = l;
    }

    public LogoFunc() {
        // TODO Auto-generated constructor stub
    }

    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("TO ");
        b.append(identifier);
        b.append("\n");
        b.append(body);
        b.append("END");
        
        return b.toString();
    }
    
    public LogoVal eval(LogoEnv e) {
        e.setFunction(identifier, this);
        //body.eval(e);
        return null;
    }

    public void eval_def(LogoEnv e) {
        e.setFunction(identifier, this);
    }
    
    public void parse(LogoLex scanner, LogoEnv env)
           throws IOException, LogoParseException {
        Yytoken token;

        if (LogoNode.debug)
            System.out.println("Parsing procedure");

        token = scanner.yylex();
        if (token == null)
            throw new LogoParseException("Expected identifier after TO\n");
        
        if (token.m_index != LogoSym.IDENTIFIER)
            throw new LogoParseException("Expected identifier after TO\n");

        this.identifier = token.m_text;

        // Check for parameters
        token = scanner.yylex();
        if (token == null)
            throw new LogoParseException("Expected parameters or body after TO\n");

        parameters = new Vector();
        while (token.m_index == LogoSym.COLON) {
            token = scanner.yylex();
            if (token.m_index != LogoSym.IDENTIFIER)
                throw new LogoParseException("Expected argument identifier after colon\n");
            parameters.addElement(token.m_text);
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected arguments or body after TO\n");
        }

        scanner.yypushback(token.m_charEnd - token.m_charBegin);
        body = new LogoCmds();
        body.parse(scanner, env);

        token = scanner.yylex();
        if (token == null)
            throw new LogoParseException("Expected END after procedure commands\n");
        
        if (token.m_index != LogoSym.END)
            throw new LogoParseException("Expected END after procedure commands, found \n" + token);
        
        eval_def(env);
    }

}
