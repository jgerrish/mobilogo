package com.thedomokun.logonodes;

import java.util.Vector;

import com.thedomokun.common.LogoEnv;

public class LogoArgs extends LogoNode {
    Vector cmds = new Vector();

    public LogoArgs(LogoArg a) {
        cmds.addElement(a);
    }

    public LogoArgs(LogoArgs a, LogoArg c) {
        for (int i = 0; i < a.getChildren().size(); i++) {
            cmds.addElement(a.getChildren().elementAt(i));
        }
        cmds.addElement(c);
    }
    
    public Vector getChildren() {
        return cmds;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cmds.size(); i++) {
            buf.append(cmds.elementAt(i));
            buf.append("\n");
        }
        
        return buf.toString();
    }
    
    public LogoVal eval(LogoEnv e) {
        for (int i = 0; i < cmds.size(); i++) {
            LogoNode cmd = (LogoNode)cmds.elementAt(i);
            //System.out.println("Evaluating " + cmd);
            cmd.eval(e);
        }
        
        return null;
    }
}
