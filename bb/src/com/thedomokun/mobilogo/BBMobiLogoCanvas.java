/**
 * 
 */
package com.thedomokun.mobilogo;

import com.thedomokun.gui.BBAlert;
import com.thedomokun.gui.Image;
import com.thedomokun.gui.BBGraphics;
import com.thedomokun.gui.BBImage;
import com.thedomokun.gui.Point;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Keypad;

import com.thedomokun.common.AlertErrorHandler;
import com.thedomokun.common.GUITurtle;
import com.thedomokun.common.LogoCanvas;
import com.thedomokun.common.LogoSym;
import com.thedomokun.common.LogoSymbol;

/**
 * @author josh
 *
 */
public class BBMobiLogoCanvas extends Field {
    //private int sleepTime = 30;
    public LogoCanvas canvas;
    private Image offScreenBuffer;

    private BBMenu moveMenu, commandMenu, menu, operatorMenu, symbolMenu;
    public BBEvalField input;
    public BBSourceField source;
    private boolean focused = false;
    //private int preferred_height;
    private final int font_height = Font.getDefault().getHeight();
    private BBAlert alert;
    private AlertErrorHandler handler;

    /**
     * @param test The initial source code
     */
    public BBMobiLogoCanvas(String test) {
        super(Field.FOCUSABLE);
        alert = new BBAlert();
        handler = new AlertErrorHandler(alert);
        canvas = new LogoCanvas(handler);
        canvas.source = test;
        
        

        moveMenu = new BBMenu("movement", new BBMenu[] {
                new BBMenu(LogoSym.FORWARD),
                new BBMenu(LogoSym.RIGHT),
                new BBMenu(LogoSym.RIGHT),
                new BBMenu(LogoSym.BACKWARD)
            });
       
       commandMenu = new BBMenu("ctrl", new BBMenu [] {
               new BBMenu(LogoSym.REPEAT),
               new BBMenu(LogoSym.TO),
               new BBMenu(LogoSym.END),
               null
       });

       operatorMenu = new BBMenu("ops", new BBMenu[] {
               new BBMenu(LogoSym.PLUS),
               new BBMenu(LogoSym.MULT),
               new BBMenu(LogoSym.DIV),
               new BBMenu(LogoSym.MINUS)
           });
       
       symbolMenu = new BBMenu("symbols", new BBMenu[] {
               null,
               new BBMenu(LogoSym.LBRACKET),
               new BBMenu(LogoSym.RBRACKET),
               null
           });

       menu = new BBMenu("menu", new BBMenu[] {
                moveMenu,
                commandMenu,
                operatorMenu,
                symbolMenu
            });

       canvas.menu = menu;
    }


    public void interpret(boolean reset) {
        canvas.interpret(reset);
        invalidate();
    }

    protected void paint(net.rim.device.api.ui.Graphics g) {
        //System.out.println("width: " + width + ", height: " + height);
        //System.out.println("invalidateing");
        if (canvas.turtle != null) {
            this.canvas.setWidth(this.getWidth());
            this.canvas.setHeight(this.getHeight());
            BBGraphics g2 = new BBGraphics(g);

            canvas.paint(g2);
        }
    }

    // TODO: Get size when paint() is first called, delay initialization until then
    public void layout(int width, int height) {
        height = height - (font_height * 3) - 10;
        setExtent(width, height);
        setPosition(0, 0);

        //System.out.println("width: " + width + ", height: " + height);
        try {
            offScreenBuffer = BBImage.createImage(width, height);
        } catch (IllegalArgumentException e) {
            return;
        }

        // Create the turtle in the center of the screen
        canvas.turtle = new GUITurtle(offScreenBuffer,
                                      new Point(width / 2, height / 2));
        canvas.offScreenBuffer = offScreenBuffer;

        interpret(false);
    }

    public void setSource(String source) {
        canvas.source = source;
        this.source.setText(source);
    }

    public String getSource() {
        return canvas.source;
    }


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

    protected int getMinContentHeight() {
        return 100;
    }

    protected int getMinContentWidth() {
        return this.canvas.getWidth();
    }


    protected int getPrefContentHeight(int arg0) {
        //System.out.println(height);
        return this.canvas.getHeight();
    }


    protected int getPrefContentWidth(int arg0) {
        return this.canvas.getWidth();
        //return MIDPMobiLogo.FONT_HEIGHT;
    }

    protected boolean traverse(int dir, int viewportWidth, int viewportHeight,
                               int[] visRect_inout) {

        if (focused == false) {
            focused = true;
            return true;
        }

        if (menu.menuEnabled()) {
            int command = menu.keyPressed(dir);
            invalidate();
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

    public void setPreferredHeight(int h) {
        //this.preferred_height = h;        
    }

    public boolean isFocusable() {
        return true;
    }
    
    protected boolean navigationClick(int status, int time) {
        if (menu.menuEnabled()) {
            canvas.disableMenu();
        } else {
            canvas.enableMenu();
        }
        invalidate();

        return true;
    }

    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);

        if (menu.menuEnabled()) {
            int command = menu.keyPressed(key);
            if (command != -1) {
                //prependSource(LogoSymbol.getSymbol(command));
                appendSource(LogoSymbol.getSymbol(command));
                //currentInputPos = 0;
            }

            invalidate();
            return false;
        } else {
            // TODO: this code assumes that digits are input using alt
            // this might change in the future
            if ((key == Keypad.getUnaltedChar('0')) ||
                    (key == Keypad.getUnaltedChar('1')) ||
                    (key == Keypad.getUnaltedChar('2')) ||
                    (key == Keypad.getUnaltedChar('3')) ||
                    (key == Keypad.getUnaltedChar('4')) ||
                    (key == Keypad.getUnaltedChar('5')) ||
                    (key == Keypad.getUnaltedChar('6')) ||
                    (key == Keypad.getUnaltedChar('7')) ||
                    (key == Keypad.getUnaltedChar('8')) ||
                    (key == Keypad.getUnaltedChar('9'))) {
                appendDigits(Keypad.map(key, Keypad.KEY_ALT));
            }
        }

        return super.keyDown(keycode, time);
    }

    protected boolean keyUp(int keyCode, int time) {
        if (!menu.menuEnabled())
            canvas.disableMenu();
        
        return super.keyUp(keyCode, time);
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        // WTF, for some reason, pearl simulator is returning STATUS_FOUR_WAY
        // TODO: test this on real devices
        if (menu.menuEnabled()) {
            int command = menu.navigationMovement(dx, dy, status, time);
            if (command != -1) {
                //prependSource(LogoSymbol.getSymbol(command));
                appendSource(LogoSymbol.getSymbol(command));
                //currentInputPos = 0;
                invalidate();
                return false;
            } else {
                invalidate();
                return true;
            }
        } else
            return false;
    }

}