package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoProcTest extends LogoNodeTestCase {

    public void testParse_argument() {
        //fail("Not yet implemented");
    }

    public LogoNode doParse(String src) {
        Yytoken cur_token = null;
        
        LogoProc node = new LogoProc();
        LogoNode.debug = true;
        try {
            setupParse(src);
        } catch (Exception e) {
            fail(e.toString());
        }
        try {
            cur_token = scanner.yylex();
        } catch (IOException e) {
            fail("Error in test definition");
        }
        try {
            node.parse(scanner, cur_token, env);
        } catch (Exception e) {
            fail(e.toString());
        }
        
        return node;
    }

    public void doLogoValTest(String source, String expected) {
        LogoProc node = null;

        node = (LogoProc)doParse(source);
        if (node == null)
            fail("Null node for print");
        if (node.symbol != LogoSym.PRINT)
            fail("Invalid symbol parsed");
        if (node.arguments == null)
            fail("Expected one argument");
        LogoVal val = (LogoVal)node.arguments.elementAt(0);
        //LogoVal val = node.eval(env);
        assertEquals(expected, val.getValue());        
    }

    public void testParse() {
        String printTest = "PRINT \"Hello";
        String print2Test = "PRINT [Hello world]";
        String print3Test = "print [I'm in bottom.]";

        doLogoValTest(printTest, "Hello");
        doLogoValTest(print2Test, "Hello world");
        doLogoValTest(print3Test, "I'm in bottom.");
    }

    public void testEval() {
        String printTest = "PRINT \"Hello";
        String print2Test = "PRINT [Hello world]";
        String print3Test = "print [I'm in bottom.]";

        LogoProc node;

        node = (LogoProc)doParse(printTest);
        LogoVal val = node.eval(env);
        //System.out.println(val);

        node = (LogoProc)doParse(print2Test);
        val = node.eval(env);
        //System.out.println(val);

        node = (LogoProc)doParse(print3Test);
        val = node.eval(env);
    }

}
