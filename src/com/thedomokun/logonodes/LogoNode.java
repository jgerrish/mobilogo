package com.thedomokun.logonodes;

import java.util.Vector;

import com.thedomokun.common.LogoEnv;

/**
 * @author josh
 *
 */
public abstract class LogoNode {
    protected LogoEnv env;
    public static boolean debug = false;

    public void setEnv(LogoEnv e) {
        this.env = e;
    }
    
    public abstract Vector getChildren();
    //public abstract void eval(LogoEnv env);
    public abstract LogoVal eval(LogoEnv env);
}
