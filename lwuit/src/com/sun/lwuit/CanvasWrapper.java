/**
 * 
 */
package com.sun.lwuit;

import com.sun.lwuit.geom.Dimension;

/**
 * @author josh
 *
 */
public class CanvasWrapper extends Component {
    private WrappedCanvas inner;

    public CanvasWrapper(WrappedCanvas inner) {
        this.inner = inner;
        getStyle().setBgTransparency(0);
        setPreferredSize(new Dimension(100, 150));        
    }

    public void paint(com.sun.lwuit.Graphics g) {
        System.out.println("repainting2");
        javax.microedition.lcdui.Graphics midpGraphics =
            (javax.microedition.lcdui.Graphics)g.getGraphics();

        midpGraphics.setClip(g.getTranslateX() + g.getClipX(),
        g.getTranslateY() + g.getClipY(), g.getClipWidth(), g.getClipHeight());
    
        midpGraphics.translate(getX() + g.getTranslateX(), getY() +
        g.getTranslateY());
    
        //inner.repaint();
        inner.paint(midpGraphics);
    
        midpGraphics.translate(-getX() - g.getTranslateX(),
                               -getY() - g.getTranslateY());
    }

    public void setWidth(int width) {
        System.out.println(width);
        super.setWidth(width);
        inner.setWidth(width);
    }

    public void setHeight(int height) {
        System.out.println(height);
        super.setHeight(height);
        inner.setHeight(height);
    }

    public void keyPressed(int keyCode) {
        if(Display.getInstance().getGameAction(keyCode) ==
            Display.GAME_FIRE) {
            setHandlesInput(!handlesInput());
        }
        inner.keyPressed(keyCode);
    }

    public void keyReleased(int keyCode) {
        inner.keyReleased(keyCode);
    }
}
