package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoProc extends LogoNode {
    protected int symbol;
    protected String identifier = null;
    protected Vector arguments;
    
    public LogoProc() {
        
    }

    public LogoProc(String i) {
        identifier = i;
    }
    
    public Vector getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (identifier != null) {
            buf.append(identifier);
        } else {
            switch (symbol) {
            case LogoSym.FORWARD: buf.append("FORWARD");
            break;
            case LogoSym.BACKWARD: buf.append("BACKWARD");
            break;
            case LogoSym.RIGHT: buf.append("RIGHT");
            break;
            case LogoSym.LEFT: buf.append("LEFT");
            break;
            }
        }
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                buf.append(" ");
                buf.append(arguments.elementAt(i));
            }
        }

        return buf.toString();
    }

    public LogoVal eval(LogoEnv env) {
        if ((symbol == LogoSym.FORWARD) ||
                   (symbol == LogoSym.BACKWARD) ||
                   (symbol == LogoSym.RIGHT) ||
                   (symbol == LogoSym.LEFT)) {
            // Otherwise it's a movement command
            int dist = 0;
            if (arguments != null) {
                LogoNode n1 = (LogoNode)arguments.elementAt(0);
                LogoVal v1 = n1.eval(env);
                String d = v1.getValue();
                dist = Integer.parseInt(d);
            }
            switch (symbol) {
            case LogoSym.FORWARD: env.turtle.forward(dist);
            break;
            case LogoSym.BACKWARD: env.turtle.backward(dist);
            break;
            case LogoSym.RIGHT: env.turtle.right(dist);
            break;
            case LogoSym.LEFT: env.turtle.left(dist);
            break;
            }
        } else if (symbol == LogoSym.PRINT) {
            if (LogoNode.debug)
                System.out.println("Evaluating PRINT");
            LogoNode node = (LogoNode)arguments.elementAt(0);
            if (node != null) {
                LogoVal val = node.eval(env);
                System.out.println("PRINT OUTPUT: " + val.getValue());
            }
        } else if (identifier != null) {
            LogoFunc c = env.getFunction(identifier);
            LogoEnv new_env = new LogoEnv(env);
            for (int i = 0; i < arguments.size(); i++) {
                LogoNode n = (LogoNode)arguments.elementAt(i);
                LogoVal v = (LogoVal)n.eval(env);
                String param = (String)c.parameters.elementAt(i);
                if (LogoNode.debug)
                    System.out.println("Binding " + v.getValue() +
                            " to " + param);
                new_env.setVariable(param, v.getValue());
            }
            c.eval(new_env);
            c.body.eval(new_env);
        }
        
        return null;
    }

    public LogoNode parse_argument(LogoLex scanner, LogoEnv env)
                    throws IOException, LogoParseException {
        LogoNode node = null;
        Yytoken token;
        token = scanner.yylex();

        if (token == null)
            throw new LogoParseException("Expected argument: " + token);

        if (token.m_index == LogoSym.INTEGER) {
            // integer literal
            node = new LogoVal(token.m_text);
        } else if (token.m_index == LogoSym.COLON) {
            // variable
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected identifier after colon: " + token);
            if (token.m_index != LogoSym.IDENTIFIER)
                throw new LogoParseException("Expected identifier after colon: " + token);

            LogoVal val = new LogoVal();
            val.setType(LogoVal.VARIABLE);
            val.setValue(token.m_text);

            node = val;
        } else if (token.m_index == LogoSym.IDENTIFIER) {
            // Either a variable or another procedure call
            if (env.getFunction(token.m_text) != null) {
                LogoProc cmd = new LogoProc();
                cmd.parse(scanner, token, env);
                node = cmd;
            }
        } else if (token.m_index == LogoSym.QUOTE) {
            // Quoted word
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected word after quote: " + token);
            if (token.m_index != LogoSym.IDENTIFIER)
                throw new LogoParseException("Expected word after quote: " + token);
            LogoVal val = new LogoVal();
            val.setType(LogoVal.LITERAL);
            val.setValue(token.m_text);
            node = val;
        } else if ((token.m_index == LogoSym.SUM) ||
                   (token.m_index == LogoSym.DIFFERENCE) ||
                   (token.m_index == LogoSym.PRODUCT) ||
                   (token.m_index == LogoSym.QUOTIENT) ||
                   (token.m_index == LogoSym.SENTENCE) ||
                   (token.m_index == LogoSym.FIRST) ||
                   (token.m_index == LogoSym.LAST) ||
                   (token.m_index == LogoSym.BUTFIRST) ||
                   (token.m_index == LogoSym.BUTLAST)) {
            LogoOp op = new LogoOp();
            op.parse(scanner, token, env);
            node = op;
        } else if ((token.m_index == LogoSym.EQUALP) ||
                   (token.m_index == LogoSym.GREATERP) ||
                   (token.m_index == LogoSym.LESSP)) {
            LogoPred pred = new LogoPred();
            pred.parse(scanner, token, env);
            node = pred;
        } else if (token.m_index == LogoSym.LBRACKET) {
            // Parsing a list
            LogoList list = new LogoList();
            list.parse(scanner, token, env);
            node = list;
        }

        // Check if there is an infix operator (+, -, *, /)
        try {
            token = scanner.yylex();
        } catch (IOException e) {
            return node;
        }

        if (token == null)
            return node;

        if ((token.m_index == LogoSym.PLUS) ||
            (token.m_index == LogoSym.MINUS) ||
            (token.m_index == LogoSym.MULT) ||
            (token.m_index == LogoSym.DIV)) {
            LogoOp op = new LogoOp();
            // Add the previous node as the first argument
            op.addArgument(node);
            op.parse(scanner, token, env);

            node = op;
        } else {
            scanner.yypushback(token.m_charEnd - token.m_charBegin);
        }

        return node;
    }

    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
                  throws IOException, LogoParseException {
        //Yytoken token;
        arguments = new Vector();
        if ((cur_token.m_index == LogoSym.FORWARD) ||
                (cur_token.m_index == LogoSym.BACKWARD) ||
                (cur_token.m_index == LogoSym.RIGHT) ||
                (cur_token.m_index == LogoSym.LEFT)) {
            symbol = cur_token.m_index;
            LogoNode node = parse_argument(scanner, env);
            arguments.addElement(node);
        } else if (cur_token.m_index == LogoSym.IDENTIFIER) {
            // Procedure call
            int num_arguments = 0;
            if (LogoNode.debug)
                System.out.println("Parsing procedure call");

            // Procedure call
            this.identifier = cur_token.m_text;
            LogoFunc func = null;
            func = env.getFunction(identifier);
            if (func == null) {
                throw new LogoParseException("No procedure called " +
                        identifier);
            } else {
                if (func.parameters != null) {
                    num_arguments = func.parameters.size();
                    LogoNode node = null;
                    for (int i = 0; i < num_arguments; i++) {
                        node = parse_argument(scanner, env);
                        arguments.addElement(node);
                    }
                }
            }
        } else if (cur_token.m_index == LogoSym.PRINT) {
            this.identifier = cur_token.m_text;
            if (LogoNode.debug)
                System.out.println("Parsing PRINT");
            this.symbol = cur_token.m_index;
            LogoNode node = parse_argument(scanner, env);
            arguments.addElement(node);
        }
    }
}
