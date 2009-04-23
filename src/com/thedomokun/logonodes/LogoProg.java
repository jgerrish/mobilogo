/**
 * 
 */
package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoParseException;

/**
 * @author josh
 *
 */
public class LogoProg extends LogoNode {
    LogoCmds cmds = null;

    public LogoProg() {
        cmds = new LogoCmds();
    }

    /* (non-Javadoc)
     * @see com.thedomokun.logonodes.LogoNode#eval(com.thedomokun.common.LogoEnv)
     */
    public LogoVal eval(LogoEnv env) {
        cmds.eval(env);

        return null;
    }
    
    /* (non-Javadoc)
     * @see com.thedomokun.logonodes.LogoNode#getChildren()
     */
    public Vector getChildren() {
        // TODO Auto-generated method stub
        return cmds.cmds;
    }

    public LogoProg parse(LogoLex scanner, LogoEnv env) throws IOException, LogoParseException {
        if (LogoNode.debug)
            System.out.println("Parsing starting at program node");

        cmds.parse(scanner, env);

        return this;
    }
}
