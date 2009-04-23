/**
 * 
 */
package com.thedomokun.mobilogo;


import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;

import com.thedomokun.common.AlertErrorHandler;
import com.thedomokun.common.GUITurtle;
import com.thedomokun.common.LogoCanvas;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.LogoSymbol;

import com.thedomokun.gui.LWUITAlert;
import com.thedomokun.gui.LWUITGraphics;
import com.thedomokun.gui.LWUITImage;
import com.thedomokun.gui.Graphics;
import com.thedomokun.gui.Point;


/**
 * @author josh
 *
 */
public class LWUITComponent extends Component {
    //private int sleepTime = 30;
    public LogoCanvas canvas;
    private LWUITImage offScreenBuffer;
    private LWUITMenu moveMenu, commandMenu, menu, operatorMenu, symbolMenu;
    public TextField input;
    //private int currentInputPos;

    /**
     * 
     */
    public LWUITComponent(Display d, String test) {
        super();
        LWUITAlert alert = new LWUITAlert(d);
        AlertErrorHandler handler = new AlertErrorHandler(alert);

        canvas = new LogoCanvas(handler);
        canvas.source = test;
       moveMenu = new LWUITMenu("movement", new LWUITMenu[] {
                new LWUITMenu(LogoSym.FORWARD),
                new LWUITMenu(LogoSym.RIGHT),
                new LWUITMenu(LogoSym.RIGHT),
                new LWUITMenu(LogoSym.BACKWARD)
            });
       
       commandMenu = new LWUITMenu("ctrl", new LWUITMenu [] {
               new LWUITMenu(LogoSym.REPEAT),
               new LWUITMenu(LogoSym.TO),
               new LWUITMenu(LogoSym.END),
               null
       });

       operatorMenu = new LWUITMenu("ops", new LWUITMenu[] {
               new LWUITMenu(LogoSym.PLUS),
               new LWUITMenu(LogoSym.MULT),
               new LWUITMenu(LogoSym.DIV),
               new LWUITMenu(LogoSym.MINUS)
           });
       
       symbolMenu = new LWUITMenu("symbols", new LWUITMenu[] {
               null,
               new LWUITMenu(LogoSym.LBRACKET),
               new LWUITMenu(LogoSym.RBRACKET),
               null
           });

       menu = new LWUITMenu("menu", new LWUITMenu[] {
                moveMenu,
                commandMenu,
                operatorMenu,
                symbolMenu
            });

       canvas.menu = menu;
    }

    /**
     * Indicate to LWUIT the component name for theming
     */
    public String getUIID() {
        return "TurtleCanvas";
    }
    
    
    //protected Dimension calcPreferredSize() {
        // we don't really need to be in the font height but this provides
        // a generally good indication for size expectations
        //return new Dimension(Display.getInstance().getDisplayWidth(), 
        //                     Font.getDefaultFont().getHeight() * 4);
    //}
    
    
    protected void paintClipRect(Graphics g) {
        int clipX = g.getClipX();
        int clipY = g.getClipY();

        int clipH = g.getClipHeight();
        int clipW = g.getClipWidth();
        int color = g.getColor();
        // lcdui accepts three ints
        //g.setColor(255, 255, 255);
        // LWUIT accepts a single int
        g.setColor(0xffffff);
        g.fillRect(clipX, clipY, clipW, clipH);
        g.setColor(color);
    }

    public void interpret(boolean reset) {
        canvas.interpret(reset);
        repaint();
    }

    public void paint(com.sun.lwuit.Graphics g) {
        LWUITGraphics g2 = new LWUITGraphics(g);
        canvas.paint(g2);
    }

    public void start() {
        // Only create buffer image if device does not support double buffering natively
        //if (!isDoubleBuffered())
        int width = getWidth();
        int height = getHeight();
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);

        offScreenBuffer = LWUITImage.createImage(width, height);
        //else
        //    System.out.println("Canvas already double buffered");
        //System.out.println("component width:" + width + ", height: " + height);
        // Create the turtle in the center of the screen

        canvas.turtle = new GUITurtle(offScreenBuffer, new Point(width / 2, height / 2));
        canvas.offScreenBuffer = offScreenBuffer;
        interpret(false);
    }

    public void setSource(String source) {
        canvas.source = source;
    }

    public String getSource() {
        return canvas.source;
    }

    /* Experimental code to do RPN input */
    /*
    private void prependSource(String text) {
        StringBuffer t = new StringBuffer(input.getText());
        t.insert(0, " ");
        t.insert(0, text);
        input.setText(t.toString());        
    }

    private void appendDigits(int c) {
        StringBuffer t = new StringBuffer(input.getText());
        if (t.length() > 0) {
            if (!((t.charAt(0) >= '0') && (t.charAt(0) <= '9')) &&
                (t.charAt(0) != ' ')) {
                t.insert(0, " ");
            }
        }
        if (currentInputPos > input.getText().length())
            currentInputPos = 0;
        t.insert(currentInputPos++, c - 48);
        input.setText(t.toString());
    }
    */

    public void appendSource(String text) {
        StringBuffer t = new StringBuffer(input.getText());
        t.append(" ");
        t.append(text);
        input.setText(t.toString());
    }
    
    public void appendDigits(int c) {
        StringBuffer t = new StringBuffer(input.getText());
        if (t.length() > 0) {
            int last_char = t.length() - 1;
            if (!((t.charAt(last_char) >= '0') && (t.charAt(last_char) <= '9')) &&
                (t.charAt(last_char) != ' ')) {
                t.append(" ");
            }
        }

        t.append(c - 48);
        input.setText(t.toString());
    }

    public void keyReleased(int keyCode) {
        if (!menu.menuEnabled())
            disableMenu();
        super.keyReleased(keyCode);
    }

    public void enableMenu() {
        canvas.enableMenu();
        setHandlesInput(true);
    }
    
    public void disableMenu() {
        canvas.disableMenu();
        setHandlesInput(false);
    }

    /**
     * Handle keyboard input.
     * @param keyCode pressed key is either Canvas arrow key (UP,
     *  DOWN, LEFT, RIGHT) or simulated with KEY_NUM (2, 8, 4,6).
     */
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        if (menu.menuEnabled()) {
            int command = menu.keyPressed(keyCode);
            if (command != -1) {
                //prependSource(LogoSymbol.getSymbol(command));
                appendSource(LogoSymbol.getSymbol(command));
                //currentInputPos = 0;
            }

            repaint();
            return;
        }

        switch(Display.getInstance().getGameAction(keyCode)) {
        case Display.GAME_UP:
            //currentInputPos = 0;
            repaint();
            break;

        case Display.GAME_DOWN:
            //currentInputPos = 0;
            repaint();
            break;

        case Display.GAME_LEFT:
            //currentInputPos = 0;
            repaint();
            break;

        case Display.GAME_RIGHT:
            //currentInputPos = 0;
            repaint();
            break;

        case Display.GAME_FIRE:
            if (menu.menuEnabled())
                disableMenu();
            else
                enableMenu();
            repaint();
            break;
        case 0:
            // There is no game action.. Use keypad constants instead
            if (!menu.menuEnabled()) {
                if ((keyCode >= '0') && (keyCode <= '9')) {
                    appendDigits(keyCode);
                }
            }
            break;
        }
    }
}
