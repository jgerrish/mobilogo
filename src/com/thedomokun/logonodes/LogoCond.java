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
import com.thedomokun.common.Yytoken;

/**
 * @author Joshua Gerrish
 *
 */
public class LogoCond extends LogoProc {

    public LogoVal eval(LogoEnv env) {
        LogoVal val = null;
        LogoPred node = (LogoPred)arguments.elementAt(0);
        LogoVal pred_val = node.eval(env);
        if (symbol == LogoSym.IF) {
            if (pred_val.getValue().equals("true")) {
                LogoCmds cmds = (LogoCmds)arguments.elementAt(1);
                return cmds.eval(env);
            }
        } else if (symbol == LogoSym.IFELSE) {
            LogoCmds cmds = null;
            if (pred_val.getValue().equals("true")) {
                cmds = (LogoCmds)arguments.elementAt(1);
            } else {
                cmds = (LogoCmds)arguments.elementAt(2);
            }
            return cmds.eval(env);
            
        }

        return val;
    }

    public LogoCmds parse_cmds(LogoLex scanner, LogoEnv env)
           throws IOException, LogoParseException {
        Yytoken token = scanner.yylex();
        if (token.m_index != LogoSym.LBRACKET)
            throw new LogoParseException("Expected [");

        LogoCmds cmds = new LogoCmds();
        cmds.parse(scanner, env);
        token = scanner.yylex();
        if (token.m_index != LogoSym.RBRACKET)
            throw new LogoParseException("Expected ]");
        
        return cmds;
    }
    
    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
    throws IOException, LogoParseException {
        symbol = cur_token.m_index;
        identifier = cur_token.m_text;
        arguments = new Vector();

        // Parse the predicate
        LogoPred node = (LogoPred)parse_argument(scanner, env);
        arguments.addElement(node);            

        if (symbol == LogoSym.IF) {
            if (LogoNode.debug)
                System.out.println("Parsing IF");
            arguments.addElement(parse_cmds(scanner, env));
        } else if (symbol == LogoSym.IFELSE) {
            if (LogoNode.debug)
                System.out.println("Parsing IFELSE");
            arguments.addElement(parse_cmds(scanner, env));
            arguments.addElement(parse_cmds(scanner, env));            
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(identifier);
        buf.append(" ");
        buf.append(arguments.elementAt(0));
        buf.append(" ");
        buf.append(arguments.elementAt(1));
        if (symbol == LogoSym.IFELSE) {
            buf.append(" ");
            buf.append(arguments.elementAt(1));
        }

        return buf.toString();
    }
}
