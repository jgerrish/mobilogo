/**
 * 
 */
package com.thedomokun.mobilogo;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Display;

import com.thedomokun.gui.Image;
import com.thedomokun.gui.MIDPAlert;
import com.thedomokun.gui.MIDPGraphics;
import com.thedomokun.gui.MIDPImage;
import com.thedomokun.gui.Point;

import javax.microedition.lcdui.TextField;

import com.thedomokun.common.GUITurtle;
import com.thedomokun.common.LogoCanvas;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.LogoSymbol;
import com.thedomokun.common.AlertErrorHandler;

/**
 * MIDPCanvas is the drawing canvas for LOGO.  This class extends the MIDP
 * CustomItem class and includes a LogoCanvs item.
 * 
 * @author josh
 *
 */
public class MIDPCanvas extends CustomItem {
    //private int sleepTime = 30;
    public LogoCanvas canvas;
    private Image offScreenBuffer;

    private MIDPMenu moveMenu, commandMenu, menu, operatorMenu, symbolMenu;
    public TextField input;
    private boolean focused = false;
    private Display display;
    private MIDPAlert alert;
    private AlertErrorHandler handler;

    /**
     * Create a MIDPCanvas
     * 
     * @param d the MIDP display object to use
     * @param test the initial canvas source code
     */
    public MIDPCanvas(Display d, String test) {
        super("");
        alert = new MIDPAlert(d);
        AlertErrorHandler handler = new AlertErrorHandler(alert);
        canvas = new LogoCanvas(handler);
        canvas.source = test;
        moveMenu = new MIDPMenu("movement", new MIDPMenu[] {
                new MIDPMenu(LogoSym.FORWARD),
                new MIDPMenu(LogoSym.RIGHT),
                new MIDPMenu(LogoSym.RIGHT),
                new MIDPMenu(LogoSym.BACKWARD)
            });
       
       commandMenu = new MIDPMenu("ctrl", new MIDPMenu [] {
               new MIDPMenu(LogoSym.REPEAT),
               new MIDPMenu(LogoSym.TO),
               new MIDPMenu(LogoSym.END),
               null
       });

       operatorMenu = new MIDPMenu("ops", new MIDPMenu[] {
               new MIDPMenu(LogoSym.PLUS),
               new MIDPMenu(LogoSym.MULT),
               new MIDPMenu(LogoSym.DIV),
               new MIDPMenu(LogoSym.MINUS)
           });
       
       symbolMenu = new MIDPMenu("symbols", new MIDPMenu[] {
               null,
               new MIDPMenu(LogoSym.LBRACKET),
               new MIDPMenu(LogoSym.RBRACKET),
               null
           });

       menu = new MIDPMenu("menu", new MIDPMenu[] {
                moveMenu,
                commandMenu,
                operatorMenu,
                symbolMenu
            });

       canvas.menu = menu;
       display = d;
    }

    /**
     * Get the canvas width
     * @return the width
     */
    public int getWidth() {
        return canvas.getWidth();
    }

    /**
     * Get the canvas height
     * @return the height
     */
    public int getHeight() {
        return canvas.getWidth();
    }

    /**
     * Interpret the current source code in the canvas.  This method repaints the
     * canvas after the interpreter runs.
     * 
     * @param reset
     */
    public void interpret(boolean reset) {
        canvas.interpret(reset);
        repaint();
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
     */
    protected void paint(javax.microedition.lcdui.Graphics g, int width, int height) {
        //System.out.println("width: " + width + ", height: " + height);
        //System.out.println("repainting");
        if (canvas.turtle != null) {
            this.canvas.setWidth(width);
            this.canvas.setHeight(height);
            MIDPGraphics g2 = new MIDPGraphics(g);

            canvas.paint(g2);
        }
    }

    // TODO: Get size when paint() is first called, delay initialization until then
    /**
     * Initialize the canvas
     */
    public void start() {
        int width, height;
        // Only create buffer image if device does not support double buffering natively
        //if (!isDoubleBuffered())
        //this.width = width;
        //this.height = height;
        //System.out.println("width: " + width + ", height: " + height);
        if ((this.canvas.getWidth() == 0) || (this.canvas.getHeight() == 0)) {
            this.canvas.setWidth(this.getPreferredWidth());
            this.canvas.setHeight(this.getPreferredHeight());
        }
        width = this.canvas.getWidth();
        height = this.canvas.getHeight();

        //System.out.println("width: " + width + ", height: " + height);
        offScreenBuffer = MIDPImage.createImage(width, height);
        //else
        //    System.out.println("Canvas already double buffered");
        // Create the turtle in the center of the screen
        canvas.turtle = new GUITurtle(offScreenBuffer, new Point(width / 2, height / 2));
        canvas.offScreenBuffer = offScreenBuffer;

        interpret(false);
    }

    /**
     * Set the canvas' current source code
     * @param source
     */
    public void setSource(String source) {
        canvas.source = source;
    }

    /**
     * Return the source code for the embedded canvas object
     * @return the source code 
     */
    public String getSource() {
        return canvas.source;
    }


    /**
     * Append source code to the single line input field.
     * 
     * @param text the text to append to the field
     */
    public void appendSource(String text) {
        StringBuffer t = new StringBuffer(input.getString());
        t.append(" ");
        t.append(text);
        input.setString(t.toString());
    }

    /**
     * Append a single digit to the single line input field
     * @param c the digit to append
     */
    public void appendDigits(int c) {
        StringBuffer t = new StringBuffer(input.getString());
        if (t.length() > 0) {
            int last_char = t.length() - 1;
            if (!((t.charAt(last_char) >= '0') && (t.charAt(last_char) <= '9')) &&
                (t.charAt(last_char) != ' ')) {
                t.append(" ");
            }
        }

        t.append(c - 48);
        input.setString(t.toString());
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#keyReleased(int)
     */
    public void keyReleased(int keyCode) {
        if (!menu.menuEnabled())
            canvas.disableMenu();
        super.keyReleased(keyCode);
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#keyPressed(int)
     */
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);

        switch (getGameAction(keyCode)) {
        case Canvas.UP:
            repaint();
            break;

        case Canvas.DOWN:
            repaint();
            break;

        case Canvas.LEFT:
            repaint();
            break;

        case Canvas.RIGHT:
            repaint();
            break;

        case Canvas.FIRE:
            if (menu.menuEnabled()) {
                canvas.disableMenu();
            } else {
                canvas.enableMenu();
            }
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

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#getMinContentHeight()
     */
    protected int getMinContentHeight() {
        return 100;
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
     */
    protected int getMinContentWidth() {
        return this.canvas.getWidth();
    }


    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#getPrefContentHeight(int)
     */
    protected int getPrefContentHeight(int arg0) {
        //System.out.println(height);
        return this.canvas.getHeight();
    }


    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#getPrefContentWidth(int)
     */
    protected int getPrefContentWidth(int arg0) {
        return this.canvas.getWidth();
        //return MIDPMobiLogo.FONT_HEIGHT;
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CustomItem#traverse(int, int, int, int[])
     */
    protected boolean traverse(int dir, int viewportWidth, int viewportHeight,
                               int[] visRect_inout) {

        if (focused == false) {
            focused = true;
            return true;
        }

        if (menu.menuEnabled()) {
            int command = menu.keyPressed(dir);
            repaint();
            if (command != -1) {
                //prependSource(LogoSymbol.getSymbol(command));
                appendSource(LogoSymbol.getSymbol(command));
                //currentInputPos = 0;
                // Return one last true so that focus doesn't switch immediately to
                // the eval
                return true;
            } else
                return true;
        } else {
            focused = false;
            return false;
        }        
    }
}
