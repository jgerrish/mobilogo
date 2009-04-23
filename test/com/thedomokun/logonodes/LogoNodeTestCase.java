package com.thedomokun.logonodes;

import java.io.ByteArrayInputStream;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoSimpleParser;
import com.thedomokun.common.TestTurtle;

import junit.framework.TestCase;

public class LogoNodeTestCase extends TestCase {
    public LogoSimpleParser parser = null;
    public LogoEnv env = null;
    public TestTurtle turtle = null;
    public LogoLex scanner;
    
    public LogoNodeTestCase() {
        parser = new LogoSimpleParser();
        turtle = new TestTurtle();
    }

    public LogoNode doParse(String source) {
        return null;
    }

    public void doLogoValTest(String source, String expected) {
        LogoNode node = null;

        node = doParse(source);
        LogoVal val = node.eval(env);
        assertEquals(expected, val.getValue());
    }


    public void setupParse(String source) throws Exception {
        ByteArrayInputStream b = new ByteArrayInputStream(source.getBytes());
        scanner = new LogoLex(b);
        parser.setScanner(scanner);

        // Start with a new environment for every parse
        env = new LogoEnv(turtle);
    }

}
