package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoOpTest extends LogoNodeTestCase {

    public LogoNode doParse(String src) {
        Yytoken cur_token = null;
        
        LogoOp node = new LogoOp();

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
        String sumTest = "SUM 1 2";
        String diffTest = "DIFFERENCE 4 3";
        String prodTest = "PRODUCT 3 4";
        String quotientTest = "QUOTIENT 6 2";

        LogoOp node;

        node = (LogoOp)doParse(sumTest);
        if (node == null)
            fail("Null node for sum");
        if (node.symbol != LogoSym.SUM)
            fail("Invalid operator symbol parsed");
        LogoVal val = (LogoVal)node.arguments.elementAt(0);
        assertEquals("1", val.getValue());
        val = (LogoVal)node.arguments.elementAt(1);
        assertEquals("2", val.getValue());
        
        node = (LogoOp)doParse(diffTest);
        if (node == null)
            fail("Null node for diff");
        if (node.symbol != LogoSym.DIFFERENCE)
            fail("Invalid operator symbol parsed");
        val = (LogoVal)node.arguments.elementAt(0);
        assertEquals("4", val.getValue());
        val = (LogoVal)node.arguments.elementAt(1);
        assertEquals("3", val.getValue());

        node = (LogoOp)doParse(prodTest);
        if (node == null)
            fail("Null node for product");
        if (node.symbol != LogoSym.PRODUCT)
            fail("Invalid operator symbol parsed");
        val = (LogoVal)node.arguments.elementAt(0);
        assertEquals("3", val.getValue());
        val = (LogoVal)node.arguments.elementAt(1);
        assertEquals("4", val.getValue());

        node = (LogoOp)doParse(quotientTest);
        if (node == null)
            fail("Null node for quotient");
        if (node.symbol != LogoSym.QUOTIENT)
            fail("Invalid operator symbol parsed");
        val = (LogoVal)node.arguments.elementAt(0);
        assertEquals("6", val.getValue());
        val = (LogoVal)node.arguments.elementAt(1);
        assertEquals("2", val.getValue());
    }

    public void doLogoValTest(String source, String expected) {
        LogoOp node = null;

        node = (LogoOp)doParse(source);
        LogoVal val = node.eval(env);
        assertEquals(expected, val.getValue());
    }

    public void testListOps() {
        doLogoValTest("first \"Hello", "H");
        doLogoValTest("first [How are you?]", "How");
        doLogoValTest("last \"Hello", "o");
        doLogoValTest("last [How are you?]", "you?");
        doLogoValTest("butfirst \"Hello", "ello");
        doLogoValTest("butfirst [How are you?]", "are you?");
        doLogoValTest("butfirst [Hello]", "");
        doLogoValTest("butfirst \"A", "");
        
        doLogoValTest("butlast \"Hello", "Hell");
        doLogoValTest("butlast [How are you?]", "How are");
    }

    public void testItemEval() {
        doLogoValTest("item 3 \"Yesterday", "s");
        doLogoValTest("item 2 [Good Day Sunshine]", "Day");
    }

    public void testListEval() {
        doLogoValTest("list [this is] [a test]", "[this is] [a test]");
        doLogoValTest("list \"this [is one too]", "this [is one too]");
        doLogoValTest("list [] [list of words]", "[] [list of words]");
    }

    public void testSentenceEval() {
        //String test5 = "sentence [[list 1a] [list 1b]] [[list 2a] [list 2b]]";
        //String test5expected = "[list 1a] [list 1b] [list 2a] [list 2b]";
        
        doLogoValTest("sentence \"hello \"goodbye", "hello goodbye");
        doLogoValTest("sentence [this is] [a test]", "this is a test");
        doLogoValTest("sentence \"this [is one too]", "this is one too");
        doLogoValTest("sentence [] [list of words]", "list of words");
        //doLogoValTest(test5, test5expected);
    }

    public void testInfix() {
        String sumTest = "SUM 1 + 2 3";
        String diffTest = "SUM 4 - 3 1";
        String prodTest = "SUM 3 * 4 1";
        String quotientTest = "SUM 6 / 2 1";

        doLogoValTest(sumTest, "6");
        doLogoValTest(diffTest, "2");
        doLogoValTest(prodTest, "13");
        doLogoValTest(quotientTest, "4");
    }

    public void testEval() {
        String sumTest = "SUM 1 2";
        String diffTest = "DIFFERENCE 4 3";
        String prodTest = "PRODUCT 3 4";
        String quotientTest = "QUOTIENT 6 2";

        doLogoValTest(sumTest, "3");
        doLogoValTest(diffTest, "1");
        doLogoValTest(prodTest, "12");
        doLogoValTest(quotientTest, "3");
    }

}
