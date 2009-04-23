package com.thedomokun.common;

import java.io.ByteArrayInputStream;

import com.thedomokun.logonodes.LogoProg;

//import java_cup.runtime.Symbol;


/**
 * @author josh
 *
 */
public class LogoInterpreter {
    private LogoSimpleParser parser;
    private LogoProg parse_tree;
    private LogoEnv env = null;
    private Turtle turtle;
    private boolean do_debug_parse = false;
    private LogoProg program = null;
    public ErrorHandler handler;

    public LogoInterpreter(Turtle t, ErrorHandler handler) {
        this.handler = handler;
        parser = new LogoSimpleParser();
        parser.setErrorHandler(handler);
        turtle = t;
    }

    public LogoProg parse(String source) throws Exception {
        ByteArrayInputStream b = new ByteArrayInputStream(source.getBytes());
        LogoLex l = new LogoLex(b);
        parser.setScanner(l);

        env = new LogoEnv(turtle);

        try {
            if (do_debug_parse)
                parse_tree = parser.debug_parse(env);
            else
                parse_tree = parser.parse(env);
        } catch (Exception e) {
            throw(e);
            /* do cleanup here - - possibly rethrow e */
        }
        
        return parse_tree;
    }
    
    public void interpret(String source, LogoEnv env) {
        try {
            parse_tree = parse(source);
        } catch (Exception e) {
            return;
        }
        //program = (LogoProg)parse_tree.value;
        program = (LogoProg)parse_tree;
        if (do_debug_parse)
            System.out.println("Done parsing, evaluating now");
        program.eval(env);
    }
    
    public void interpret(String source) {
        if (env == null)
            env = new LogoEnv(turtle);
        interpret(source, env);
    }    
}
