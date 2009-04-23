package com.thedomokun.common;

//import java.util.Enumeration;
import java.util.Hashtable;
import com.thedomokun.logonodes.LogoFunc;

/**
 * Class that represents a LOGO environment.  The environment contains
 * the current state of the LOGO interpreter.  This includes variables,
 * procedures, and the state of the turtle.
 * 
 * LOGO uses dynamic scoping, and currently it uses deep binding.  If a
 * variable is not found in the current environment, parent environments
 * are searched.
 * 
 * @author Joshua Gerrish
 *
 */
public class LogoEnv {
    private LogoEnv parent = null;
    private Hashtable variables;
    private Hashtable functions;
    public Turtle turtle;

    // Copy constructor
    /*
    public LogoEnv(LogoEnv env) {
        variables = new Hashtable();
        functions = new Hashtable();
        for (Enumeration e = env.variables.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            variables.put(key, env.variables.get(key));
        }
        for (Enumeration e = env.functions.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            variables.put(key, env.functions.get(key));
        }
        turtle = env.turtle;
    }
    */
    
    /**
     * Create a new environment, with a pointer to the previous environment
     * 
     * @param env the calling environment
     */
    public LogoEnv(LogoEnv env) {
        variables = new Hashtable();
        functions = new Hashtable();
        this.parent = env;
        this.turtle = env.turtle;
    }

    /**
     * Create a new environment.
     * 
     * @param turtle the turtle to use for this environment.
     */
    public LogoEnv(Turtle turtle) {
        variables = new Hashtable();
        functions = new Hashtable();
        this.turtle = turtle;
    }

    /**
     * Set the turtle for this environment
     * 
     * @param t the turtle
     */
    public void setTurtle(Turtle t) {
        this.turtle = t;
    }

    /**
     * Bind the variable to the value in the current environment.
     * This will mask any previous bindings.
     * 
     * @param var the variable to put
     * @param val the value of the variable
     */
    public void setVariable(String var, String val) {
        variables.put(var, val);
    }

    /**
     * Search the current environment frame for the variable
     * If the variable is not found, recursively search the calling frames.
     * 
     * @param var the variable to lookup in the environment
     * @return the variable
     */
    public String getVariable(String var) {
        String v = null;
        v = (String)variables.get(var);
        if (v == null) {
            if (this.parent == null)
                return null;
            else
                return parent.getVariable(var);
        } else {
            return v;
        }
    }
    
    /**
     * Bind a procedure name to a procedure in this environment, masking
     * any previous bindings.
     * 
     * @param name the name of the procedure
     * @param func the procedure to bind
     */
    public void setFunction(String name, LogoFunc func) {
        functions.put(name, func);
    }

    /**
     * Search this environment frame for a procedure.
     * If the procedure is not found, recursively search the calling frames.
     * 
     * @param name the procedure name
     * @return the procedure
     */
    public LogoFunc getFunction(String name) {
        LogoFunc f = null;
        f = (LogoFunc)functions.get(name);
        if (f == null) {
            if (this.parent == null)
                return null;
            else
                return parent.getFunction(name);
        } else
            return f;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        return toStringHelper(buf, 0);
    }
    
    public String toStringHelper(StringBuffer buf, int indent) {
        /*
        buf.append("Variables:\n");
        for (Enumeration e = variables.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            buf.append(key);
            buf.append(": ");
            buf.append((String)variables.get(key));
            buf.append("\n");
        }
        buf.append("Procedures:\n");
        for (Enumeration e = functions.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            buf.append(key);
            //buf.append(": ");
            //buf.append(functions.get(key));
            buf.append("\n");
        }
        if (parent != null)
            return toStringHelper(buf, indent + 1);
        else
            return buf.toString();
        */
        return "";
    }
}
