package com.thedomokun.common;


import com.thedomokun.logonodes.LogoNode;
import com.thedomokun.logonodes.LogoProg;

import junit.framework.TestCase;

public class LogoInterpreterTest extends TestCase {
    private StdoutErrorHandler handler;
    private TestTurtle turtle;
    private LogoInterpreter interpreter;
    
    public void testParse() {
        //fail("Not yet implemented");
    }

    public void testInterpretString() {
        handler = new StdoutErrorHandler();
        turtle = new TestTurtle();
        interpreter = new LogoInterpreter(turtle, handler);
        //LogoEnv env = new LogoEnv();

        // The below fragment is from Computer Science Logo Style v1. ch3
        // This is to test variable scope
        String test = new String();
        test += "to bottom :inner\n";
        //test += "print [I'm in bottom.]\n";
        test += "print [Im in bottom.]\n";
        test += "print sentence [:outer is] :outer\n";
        test += "print sentence [:inner is] :inner\n";
        test += "end\n";
        test += "to top :outer :inner\n";
        test += "print [I'm in top.]\n";
        test += "print sentence [:outer is] :outer\n";
        test += "print sentence [:inner is] :inner\n";
        test += "bottom \"x\n";
        test += "print [I'm in top again.]\n";
        test += "print sentence [:outer is] :outer\n";
        test += "print sentence [:inner is] :inner\n";
        test += "end\n";
        test += "top \"a \"b";

        try {
            interpreter.interpret(test);
            //prog.eval(env);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
