package com.thedomokun.logonodes;

import java.io.IOException;
import java.util.Vector;

import com.thedomokun.common.LogoEnv;
import com.thedomokun.common.LogoLex;
import com.thedomokun.common.LogoParseException;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.Yytoken;

public class LogoCmds extends LogoNode {
    Vector cmds = new Vector();

    public LogoCmds() {
    }
    
    public void addCommand(LogoProc cmd) {
        cmds.addElement(cmd);
    }
    
    public LogoCmds(LogoFunc f) {
        cmds.addElement(f);
    }

    public LogoCmds(LogoProc c) {
        cmds.addElement(c);
    }

    public LogoCmds(LogoRepeat r) {
        cmds.addElement(r);
    }
    
    public LogoCmds(LogoCmds a, LogoProc c) {
        for (int i = 0; i < a.getChildren().size(); i++) {
            cmds.addElement(a.getChildren().elementAt(i));
        }
        cmds.addElement(c);
    }
    
    public Vector getChildren() {
        return cmds;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cmds.size(); i++) {
            buf.append(cmds.elementAt(i));
            buf.append("\n");
        }
        
        return buf.toString();
    }
    
    public LogoVal eval(LogoEnv e) {
        for (int i = 0; i < cmds.size(); i++) {
            LogoNode cmd = (LogoNode)cmds.elementAt(i);
            //System.out.println("Evaluating " + cmd);
            cmd.eval(e);
        }
        
        return null;
    }

    public void parse(LogoLex scanner, LogoEnv env)
           throws IOException, LogoParseException {
        Yytoken token;

        while ((token = scanner.yylex()) != null) {
            // Movement commands
            if ((token.m_index == LogoSym.FORWARD) ||
                (token.m_index == LogoSym.BACKWARD) ||
                (token.m_index == LogoSym.RIGHT) ||
                (token.m_index == LogoSym.LEFT)) {
                if (LogoNode.debug)
                    System.out.println("Parsing movement command");
                LogoProc move_cmd = new LogoProc();
                move_cmd.parse(scanner, token, env);
                addCommand(move_cmd);
            } else if ((token.m_index == LogoSym.SUM) ||
                       (token.m_index == LogoSym.DIFFERENCE) ||
                       (token.m_index == LogoSym.PRODUCT) ||
                       (token.m_index == LogoSym.QUOTIENT)) {
                if (LogoNode.debug)
                    System.out.println("Parsing math operator");
                LogoOp math_op = new LogoOp();
                math_op.parse(scanner, token, env);
                addCommand(math_op);
            } else if (token.m_index == LogoSym.REPEAT) {
                LogoRepeat repeat_cmd = new LogoRepeat();
                repeat_cmd.parse(scanner, token, env);
                addCommand(repeat_cmd);
            } else if ((token.m_index == LogoSym.IF) ||
                       (token.m_index == LogoSym.IFELSE)) {
                LogoCond cond_cmd = new LogoCond();
                cond_cmd.parse(scanner, token, env);
                addCommand(cond_cmd);
            } else if (token.m_index == LogoSym.TO) {
                LogoFunc func_cmd = new LogoFunc();
                func_cmd.parse(scanner, env);
                addCommand(func_cmd);
            } else if (token.m_index == LogoSym.MAKE) {
                LogoMake make_cmd = new LogoMake();
                make_cmd.parse(scanner, token, env);
                addCommand(make_cmd);
            } else if (token.m_index == LogoSym.IDENTIFIER) {
                LogoProc proc_cmd = new LogoProc();
                proc_cmd.parse(scanner, token, env);
                addCommand(proc_cmd);
            } else if (token.m_index == LogoSym.PRINT) {
                LogoProc print_cmd = new LogoProc();
                print_cmd.parse(scanner, token, env);
                addCommand(print_cmd);
            } else if (token.m_index == LogoSym.SENTENCE) {
                LogoOp sentence_cmd = new LogoOp();
                sentence_cmd.parse(scanner, token, env);
                addCommand(sentence_cmd);
            } else if ((token.m_index == LogoSym.EQUALP) ||
                       (token.m_index == LogoSym.LESSP) ||
                       (token.m_index == LogoSym.GREATERP)) {
                LogoPred pred_cmd = new LogoPred();
                pred_cmd.parse(scanner, token, env);
                addCommand(pred_cmd);
            } else {
                // TODO: Is this the best way to handle lookahead?  Using yypushback?
                //System.out.println("Pushing back " + (token.m_charEnd - token.m_charBegin));
                //Dialog.inform("Pushing back " + token.m_text);
                scanner.yypushback(token.m_charEnd - token.m_charBegin);
                break;
            }
        }        
    }

}
