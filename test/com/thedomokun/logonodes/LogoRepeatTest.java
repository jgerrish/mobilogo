package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoRepeatTest extends LogoNodeTestCase {

    public LogoNode doParse(String src) {
        Yytoken cur_token = null;
        
        LogoRepeat node = new LogoRepeat();

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

    public void testEval() {
        String test1 = "REPEAT 4 [ FD 30 ]";
        String test2 = "REPEAT 0 [ FD 30 ]";

        LogoRepeat node;

        node = (LogoRepeat)doParse(test1);
        if (node == null)
            fail("Null node for REPEAT");
        if (node.symbol != LogoSym.REPEAT)
            fail("Invalid operator symbol parsed");

        node.eval(env);
        assertEquals(120, turtle.x);

        turtle.reset();

        node = (LogoRepeat)doParse(test2);
        if (node == null)
            fail("Null node for REPEAT");
        if (node.symbol != LogoSym.REPEAT)
            fail("Invalid operator symbol parsed");

        node.eval(env);
        assertEquals(0, turtle.x);
    }

    public void testParse() {
        String test1 = "REPEAT 4 [ FD 30 ]";
        //String test2 = "REPEAT 0 [ FD 30 ]";

        LogoRepeat node;

        node = (LogoRepeat)doParse(test1);
        if (node == null)
            fail("Null node for REPEAT");
        if (node.symbol != LogoSym.REPEAT)
            fail("Invalid operator symbol parsed");

    }

}
