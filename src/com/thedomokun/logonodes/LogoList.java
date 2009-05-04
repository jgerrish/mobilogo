package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoList extends LogoVal {
    private Vector list;

    public LogoList() {
        list = new Vector();
    }
    
    public String getValue() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size(); i++) {
            if (i > 0)
                buf.append(" ");
            buf.append(getMember(i));
        }

        return buf.toString();
    }

    public LogoVal eval(LogoEnv env) {
        return this;
    }

    public boolean parse_member(LogoLex scanner, Yytoken cur_token, LogoEnv env)
           throws IOException, LogoParseException {
        // TODO: combine this and parse_argument
        Yytoken token;
        token = scanner.yylex();
        
        //if (LogoNode.debug)
        //    System.out.println("Parsing " + token.m_text);

        if (token.m_index == LogoSym.INTEGER) {
            // integer literal
            list.addElement(new LogoVal(token.m_text));

            return true;
        } else if (token.m_index == LogoSym.COLON) {
            // Not actually a variable, since we're in a quoted (literal) list
            token = scanner.yylex();
            if (token == null)
                throw new LogoParseException("Expected identifier after colon: " + token);
            if (token.m_index != LogoSym.IDENTIFIER)
                throw new LogoParseException("Expected identifier after colon: " + token);

            LogoVal val = new LogoVal();
            val.setType(LogoVal.LITERAL);
            val.setValue(":" + token.m_text);
            list.addElement(val);

            return true;
        } else if (token.m_index == LogoSym.IDENTIFIER) {
            // Either a variable or another procedure call
            if (env.getFunction(token.m_text) != null) {
                LogoProc cmd = new LogoProc();
                cmd.parse(scanner, token, env);
                list.addElement(cmd);
                return true;
            } else {
                LogoVal val = new LogoVal();
                val.setType(LogoVal.LITERAL);
                val.setValue(token.m_text);
                list.addElement(val);
                return true;
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
            list.addElement(val);
            return true;
        } else if (token.m_index == LogoSym.LBRACKET) {
            // This list contains a list
            LogoList sublist = new LogoList();
            sublist.parse(scanner, token, env);
            list.addElement(sublist);
            return true;
        } else if (token.m_index == LogoSym.RBRACKET) {
            return false;
        } else {
            // Assume this is a function
            LogoVal val = new LogoVal();
            val.setType(LogoVal.LITERAL);
            val.setValue(token.m_text);
            list.addElement(val);

            return true;
        }
    }

    public void parse(LogoLex scanner, Yytoken cur_token, LogoEnv env)
           throws IOException, LogoParseException {
        //Yytoken token;

        if (cur_token.m_index != LogoSym.LBRACKET)
            throw new LogoParseException("Expected a list");

        if (LogoNode.debug)
            System.out.println("Parsing list");
        
        this.type = LIST;
        boolean has_more = true;
        
        while (has_more) {
            has_more = parse_member(scanner, cur_token, env);
        }
    }
    
    public void addElement(LogoVal val) {
        list.addElement(val);
    }

    public LogoVal first() {
        if (list.size() > 0)
            return getMember(0);
        else
            return null;
    }

    public LogoVal last() {
        if (list.size() > 0)
            return getMember(list.size() - 1);
        else
            return null;
    }

    public LogoVal butfirst() {
        LogoList l = new LogoList();
        for (int i = 1; i < list.size(); i++)
            l.addElement((LogoVal)list.elementAt(i));

        return l;
    }

    public LogoVal butlast() {
        LogoList l = new LogoList();
        for (int i = 0; i < list.size() - 1; i++)
            l.addElement((LogoVal)list.elementAt(i));

        return l;
    }
    
    public LogoVal item(int i) {
        if ((i < 1) || (i > list.size()))
                return null;
        return getMember(i - 1);
    }

    public Vector getMembers() {
        return list;
    }

    public LogoVal getMember(int index) {
        return (LogoVal)list.elementAt(index);
    }
    
    public int size() {
        return list.size();
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append(getValue());
        buf.append("]");
        return buf.toString();
    }
}
