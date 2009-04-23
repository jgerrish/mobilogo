package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.Yytoken;

public class LogoPredTest extends LogoNodeTestCase {

    public LogoNode doParse(String src) {
        Yytoken cur_token = null;
        
        LogoPred node = new LogoPred();

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
            System.out.println(node);
            fail(e.toString());
        }
        
        return node;
    }

    public void testParse() {
        
    }

    public void testLesspEval() {
        
    }

    public void testGreaterpEval() {
        doLogoValTest("GREATERP 5 4", "true");
        doLogoValTest("GREATERP 4 5", "false");
        doLogoValTest("GREATERP 4 4", "false");
    }
    
    public void testEqualpEval() {
        doLogoValTest("EQUALP 4 4", "true");
        // TODO: Add double values to interpreter
        //doLogoValTest("EQUALP 4.0 4", "true");
        doLogoValTest("EQUALP 4 3", "false");
        doLogoValTest("EQUALP \"Hello \"Hello", "true");
        doLogoValTest("EQUALP \"Hello \"There", "false");
        doLogoValTest("EQUALP [Hello there] [Hello there]", "true");
        doLogoValTest("EQUALP [Hello there] [Hello]", "false");
        doLogoValTest("EQUALP [Hello there] \"There", "false");
    }
}
