/**
 * 
 */
package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

/**
 * @author josh
 *
 */
public class LogoMake extends LogoProc {
    private String variable;
    private LogoNode value;

    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("MAKE :" + variable + " " + value);
        return buf.toString();
    }

    public LogoVal eval(LogoEnv env) {
        if (LogoNode.debug)
            System.out.println("Evaluating MAKE");
        
        LogoVal val = value.eval(env);
        String var = val.getValue();
        env.setVariable(variable, var);
        return null;
    }
    
    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
    throws IOException, LogoParseException {
        Yytoken token;
        if (cur_token.m_index == LogoSym.MAKE) {
            if (LogoNode.debug)
                System.out.println("Parsing MAKE");

            // Variable binding
            // TODO: Handle more than literal values
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected quoted variable name: " +
                        token);
            if (token.m_index != LogoSym.QUOTE)
                throw new LogoParseException("Expected quoted variable: " +
                        token);
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected variable name: " +
                        token);
            if (token.m_index != LogoSym.IDENTIFIER)
                throw new LogoParseException("Expected variable name: " +
                        token);

            variable = token.m_text;

            value = parse_argument(scanner, env);
        }
    }
}
