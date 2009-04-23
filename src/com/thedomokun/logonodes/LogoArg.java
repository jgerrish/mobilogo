package com.thedomokun.logonodes;

import java.util.Vector;

import com.thedomokun.common.LogoEnv;

public class LogoArg extends LogoNode {
    public final static int VARIABLE = 1;
    public final static int LITERAL = 2;
    public String identifier;
    public int type;

    public LogoArg(int t, String i) {
        type = t;
        identifier = i;
    }

    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(":");
        b.append(identifier);
        
        return b.toString();
    }

    public LogoVal eval(LogoEnv e) {
        //LogoCmds body = null;
        //e.setFunction(identifier, body);
        
        return null;
    }

}
