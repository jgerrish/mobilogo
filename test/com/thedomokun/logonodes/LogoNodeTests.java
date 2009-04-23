package com.thedomokun.logonodes;

import junit.framework.Test;
import junit.framework.TestSuite;

public class LogoNodeTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.thedomokun.logonodes");
        //$JUnit-BEGIN$
        //suite.addTestSuite(LogoNodeTestCase.class);
        suite.addTestSuite(LogoCondTest.class);
        suite.addTestSuite(LogoOpTest.class);
        suite.addTestSuite(LogoPredTest.class);
        suite.addTestSuite(LogoProcTest.class);
        suite.addTestSuite(LogoRepeatTest.class);
        //$JUnit-END$
        return suite;
    }

}
