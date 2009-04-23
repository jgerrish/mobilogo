/**
 * 
 */
package com.thedomokun.common;

import java.io.IOException;

import com.thedomokun.logonodes.LogoNode;
import com.thedomokun.logonodes.LogoProg;


/**
 * @author josh
 *
 */
public class LogoSimpleParser {
    private ErrorHandler handler = null;
    private LogoLex scanner = null;

    public void setErrorHandler(ErrorHandler h) {
        this.handler = h;
    }
    
    public void setScanner(LogoLex l) {
        this.scanner = l;
    }
    
    // We use top-down parsing, starting from the "prog" type
    public LogoProg parse(LogoEnv env) {
        LogoProg prog = new LogoProg();
        try {
            prog.parse(scanner, env);
        } catch (IOException e) {
            System.out.println(e);
        } catch (LogoParseException e) {
            System.out.println(e);
        }

        return prog;
    }
    
    public LogoProg debug_parse(LogoEnv env) {
        LogoNode.debug = true;
        return parse(env);
    }
}
