/**
 * 
 */
package com.thedomokun.logonodes;

import java.io.IOException;

import com.thedomokun.common.Yytoken;

/**
 * @author Joshua Gerrish
 *
 */
public class LogoCondTest extends LogoNodeTestCase {

    public LogoNode doParse(String src) {
        Yytoken cur_token = null;
        
        LogoCond node = new LogoCond();

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

    public void testCondEval() {
        //LogoNode node = doParse("if equalp 2 1 + 1 [sum 2 3]");
        //LogoNode node = doParse("if equalp 2 1 [sum 2 3]");
        //System.out.println(node);
        LogoCond cond = (LogoCond)doParse("if equalp 2 1 + 1 [print \"Yup.]");
        cond.eval(env);
        cond = (LogoCond)doParse("if equalp 3 2 [print \"Nope.]");
        cond.eval(env);
    }

    public void testCondParse() {
        //LogoNode node = doParse("if equalp 2 1 + 1 [sum 2 3]");
        //LogoNode node = doParse("if equalp 2 1 [sum 2 3]");
        //System.out.println(node);
        LogoCond cond = (LogoCond)doParse("if equalp 2 1 + 1 [print \"Yup.]");
        assertEquals("if EQUALP 2 + 1 1 print Yup.\n", cond.toString());
        cond = (LogoCond)doParse("if equalp 3 2 [print \"Nope.]");
        assertEquals("if EQUALP 3 2 print Nope.\n", cond.toString());
    }

}
