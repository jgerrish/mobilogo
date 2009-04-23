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
 * @author josh
 *
 */
public class LogoCmd extends LogoProc {
    //private LogoArg arg = null;

    public LogoCmd() {
        symbol = -1;
    }

    public LogoCmd(String i) {
        identifier = i;
    }
    
    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (symbol != -1) {
            buf.append(identifier);
        } else {
        }

        return buf.toString();
    }

    public LogoVal eval(LogoEnv env) {
        if (symbol != -1) {
        }
        
        return null;
    }

    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
                  throws IOException, LogoParseException {
        //Yytoken token;
        if ((cur_token.m_index == LogoSym.PRINT)) {
        //    token = scanner.yylex();
            
        }

    }

}
