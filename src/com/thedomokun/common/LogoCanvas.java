/**
 * 
 */
package com.thedomokun.common;

import com.thedomokun.gui.Graphics;
import com.thedomokun.gui.Image;
import com.thedomokun.gui.Menu;
import com.thedomokun.gui.Point;

/**
 * Base class for the LOGO canvas
 * 
 * @author Joshua Gerrish
 *
 */
public class LogoCanvas {
    public GUITurtle turtle;
    public Image offScreenBuffer;
    public String source;
    public Menu menu;
    protected boolean menuEnabled;
    private int width, height;
    private ErrorHandler handler;

    public LogoCanvas(ErrorHandler handler) {
        this.handler = handler;
    }

    /**
     * The current width of the LOGO canvas.
     * 
     * @return the canvas width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * The current height of the LOGO canvas.
     * 
     * @return the canvas height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Enable the procedure/variable shortcut menu.
     */
    public void enableMenu() {
        menu.resetMenu();
        menu.enableMenu();
    }

    /**
     * Disable the procedure/variable shortcut menu.
     */
    public void disableMenu() {
        menu.disableMenu();
    }

    /**
     * Set the width of the canvas
     * @param w the width of the canvas
     */
    public void setWidth(int w) {
        width = w;
    }
    
    /**
     * Set the height of the canvas
     * 
     * @param h the height of the canvas
     */
    public void setHeight(int h) {
        height = h;
    }

    /**
     * Repaint the canvas
     */
    public void repaint() {
    }

    /**
     * Interpret the canvas's source 
     * 
     * @param reset if reset is true, reset the turtle
     */
    public void interpret(boolean reset) {
        //System.out.println("Interpreting");
        // Clear the screen
        paintClipRect(offScreenBuffer.getGraphics());
        if (reset)
            turtle.reset(new Point(getWidth() / 2, getHeight() / 2));

        if (source != null) {
            LogoInterpreter interpreter = new LogoInterpreter(turtle, handler);
            interpreter.interpret(source);
            //System.out.println("Done interpreting");
        }
        repaint();
    }    

    /**
     * Clear the current canvas
     * 
     * @param g the graphics context
     */
    public void paintClipRect(Graphics g) {
        int clipX = g.getClipX();
        int clipY = g.getClipY();
         
        int clipH = g.getClipHeight();
        int clipW = g.getClipWidth();
        int color = g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(clipX, clipY, clipW, clipH);
        g.setColor(color);
    } 

    /**
     * Paint the canvas.
     * 
     * @param g the graphics object to paint to
     */
    public void paint(Graphics g) {
        paintClipRect(g);
        g.setColor(0x000000);
        if (menu.menuEnabled())
            menu.drawMenu(g);
        else
            turtle.draw(g);
    }

}
