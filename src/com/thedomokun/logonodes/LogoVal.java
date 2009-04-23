/**
 * 
 */
package com.thedomokun.logonodes;

import java.util.Vector;

import com.thedomokun.common.LogoEnv;

/**
 * @author josh
 *
 */
public class LogoVal extends LogoNode {
    public final static int VARIABLE = 1;
    public final static int LITERAL = 2;
    public final static int LIST = 3;
    public String variable = null;
    public String value = null;
    public LogoList list = null;
    public int type;

    public LogoVal() {
    }

    public LogoVal(String value) {
        type = LITERAL;
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
    public void setValue(String value) {
        if (type == LITERAL)
            this.value = value;
        else if (type == VARIABLE)
            this.variable = value;
        //else if (type == LIST)
        //    list = value;
    }

    public String getValue() {
        return value;
    }

    public LogoVal item(int i) {
        if ((i < 1) || (i > this.value.length()))
                return null;
        if (type == LITERAL)
            return new LogoVal(this.value.substring(i - 1, i));
        return null;
    }
    
    public LogoVal first() {
        if (type == LITERAL)
            return new LogoVal(this.value.substring(0, 1));
        return null;
    }

    public LogoVal last() {
        if (type == LITERAL)
            return new LogoVal(this.value.substring(this.value.length() - 1,
                                                    this.value.length()));
        return null;
    }

    public LogoVal butfirst() {
        if (type == LITERAL)
            return new LogoVal(this.value.substring(1, this.value.length()));
        return null;
    }

    public LogoVal butlast() {
        if (type == LITERAL)
            return new LogoVal(this.value.substring(0, this.value.length() - 1));
        return null;
    }

    /* (non-Javadoc)
     * @see com.thedomokun.logonodes.LogoNode#eval(com.thedomokun.common.LogoEnv)
     */
    public LogoVal eval(LogoEnv env) {
        if (type == VARIABLE) {
            this.value = env.getVariable(variable);
        }
        
        return this;
    }

    /* (non-Javadoc)
     * @see com.thedomokun.logonodes.LogoNode#getChildren()
     */
    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        //b.append(":");
        b.append(value);
        
        return b.toString();
    }

}
