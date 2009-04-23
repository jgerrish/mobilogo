package com.thedomokun.mobilogo;


import net.rim.device.api.ui.Keypad;

import com.thedomokun.gui.Graphics;
import com.thedomokun.gui.Font;
import com.thedomokun.gui.Menu;
import com.thedomokun.gui.BBFont;

import com.thedomokun.common.LogoSymbol;


/**
 * @author josh
 *
 * Most of this code is from the wonderful Calc project:
 * http://midp-calc.sourceforge.net/Calc.html
 * 
 */
public class BBMenu extends Menu {
    private BBFont menuFont;
    private Font boldMenuFont;
    private Font smallMenuFont;
    private Font smallBoldMenuFont;
    private Font numberFont;

    private static final int TOP_LEFT =
        net.rim.device.api.ui.Graphics.TOP | net.rim.device.api.ui.Graphics.LEFT;

    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
    private static final int CENTER = 4;
    
    private int dx_sum = 0, dy_sum = 0;
    
    private static final int menuColor [] = {
        0x00e0e0,0x00fc00,0xe0e000,0xfca800,0xfc5400,0xfc0000,0xc00080
    };

    // get Details from CmdStr class
    /*
    Menu(int c) {
        label = CmdDesc.getStr(c, true);
        command = c;
        flags = CmdDesc.getFlags(c);
    }
    */

    public BBMenu(String l, BBMenu [] m) {
        super();
        label = l;
        subMenu = m;
    }

    public void init_fonts() {
        
        menuFont = (BBFont)BBFont.getFont(Font.FACE_PROPORTIONAL,
                                              Font.STYLE_PLAIN,
                                              12);
        boldMenuFont = (BBFont)BBFont.getFont(Font.FACE_PROPORTIONAL,
                                              Font.STYLE_BOLD,
                                              12);
        smallMenuFont = (BBFont)BBFont.getFont(Font.FACE_PROPORTIONAL,
                                                   Font.STYLE_PLAIN,
                                                   10);
        smallBoldMenuFont = (BBFont)BBFont.getFont(Font.FACE_PROPORTIONAL,
                                                       Font.STYLE_BOLD,
                                                       10);        
    }

    public BBMenu(int c) {
        super();
        label = LogoSymbol.getSymbol(c);
        command = c;
    }
    
    private int getWidth(Graphics g) {
        return g.getClipWidth();
    }
    
    private int getHeight(Graphics g) {
        return g.getClipHeight();
    }

    protected void sizeChanged(int w, int h) {
        // Menu position
        menuW = 21+4*2;
        if (menuW<boldMenuFont.stringWidth("m/ft")+3*2)
            menuW = boldMenuFont.stringWidth("m/ft")+3*2;
        menuW = boldMenuFont.stringWidth("acosh")*2+3*2+menuW;
        if (menuW<(menuFont.stringWidth("thousand")+2*2)*2)
            menuW = (menuFont.stringWidth("thousand")+2*2)*2;
        if (menuW>w) menuW = w;
        menuH = menuFont.getHeight()*2+3*2+5*2+21;
        if (menuH>h) menuH = h;
        menuX = (w-menuW)/2;
        menuY = header+(h-menuH-header-footer)/2;
        if (menuY-menuFont.getHeight()-1<header)
            menuY = header+menuFont.getHeight()+1;
        if (menuY+menuH > h)
            menuY = h-menuH;

        // Number font
        nDigits = w/numberWidth;
        offX = (w-nDigits*numberWidth)/2;
        nLines = (h-header-footer)/numberHeight;
        offY = (h-header-footer-nLines*numberHeight)/2 + header;
        nLinesMonitor = (h-header-footer-4)/numberHeight;
        offYMonitor = (h-header-footer-
                       nLinesMonitor*numberHeight)/4+header;
        offY2 = 3*(h-header-footer-
                   nLinesMonitor*numberHeight)/4+header;
        //calc.setMaxWidth(nDigits);
        //calc.setMaxMonitorSize(nLinesMonitor-1);
    }

    private void setNumberFont(Graphics g) {
        numberFont = BBFont.getFont(Font.FACE_PROPORTIONAL,
                                  Font.STYLE_PLAIN,Font.SIZE_SMALL);
        numberWidth = numberFont.charWidth('A');
        numberHeight = numberFont.getHeight();
        sizeChanged(getWidth(g),getHeight(g));
    }

    public static int getBaselinePosition(Font f) {
        int b = f.getBaseline();
        //int b = 0;
        if (b < f.getHeight()/2) // Obviously wrong
            return f.getHeight()*19/22;
        return b;
    }

    private int labelWidth(String label, boolean bold) {
        Font normalFont = bold ? boldMenuFont : menuFont;
        //Font smallFont = bold ? smallBoldMenuFont : smallMenuFont;
        if (plainLabel(label))
            return normalFont.stringWidth(label);
        int width = 0;
        Font font = normalFont;
        for (int i=0; i<label.length(); i++) {
            char c = label.charAt(i);
            width += font.charWidth(c);
        }
        return width;
    }

    private void drawLabel(Graphics g, String label, boolean bold, int x,int y)
    {
        Font normalFont = bold ? boldMenuFont : menuFont;
        Font smallFont = bold ? smallBoldMenuFont : smallMenuFont;
        Font font = normalFont;
        g.setFont(font);
        if (plainLabel(label)) {
            g.drawString(label, x, y, TOP_LEFT);
            //g.drawString(label, x, y);
            return;
        }
        boolean sub=false,sup=false,overline=false;
        for (int i=0; i<label.length(); i++) {
            char c = label.charAt(i);
            if (sub)
                g.drawChar(c,x,y+normalFont.getHeight()-
                           getBaselinePosition(smallFont),TOP_LEFT);
            else if (sup)
                g.drawChar(c,x,y-1,TOP_LEFT);
            else
                g.drawChar(c,x,y,TOP_LEFT);
            if (overline) {
                g.drawLine(x-1,y,x+font.charWidth(c)-1,y);
                if (bold)
                    g.drawLine(x-1,y+1,x+font.charWidth(c)-1,y+1);
            }
            x += font.charWidth(c);
        }
    }

    private void drawMenuItem(Graphics g, BBMenu menu, int x, int y, int anchor)
    {
        // TODO: fix anchor code
        if (menu == null)
            return;
        boolean bold = (menu.subMenu == null) &&
        //    (menu.flags&CmdDesc.SUBMENU_REQUIRED)==0;
              (menu.flags == 0);
        int width = labelWidth(menu.label, bold);
        // TODO: Fix Graphics.* code
        if (anchor == RIGHT)
            x -= width;
        else if ((anchor == TOP) || (anchor == BOTTOM))
            x -= width/2;
        if (anchor == BOTTOM)
            y -= menuFont.getHeight();
        drawLabel(g,menu.label,bold,x,y);
    }

    public void drawMenu(Graphics g) {
        setNumberFont(g);
        if (menuFont == null) {
            // Error getting system font.  Set them again
            init_fonts();
            if (menuFont == null) {
                System.out.println("Failed getting system font");
                return;
            }
        }

        int w = menuW;
        int h = menuH;
        int x = menuX;
        int y = menuY;
        //System.out.println("x: " + x + ", y: " + y + "w: " + w + "h: " + h);
        int ym = ((y + h - 3) - menuFont.getHeight() + (y + 3)) / 2;
        // Draw menu title
        //System.out.println(menuStackPtr);
        g.setColor(menuColor[menuStackPtr] / 4);
        g.fillRect(x,y-menuFont.getHeight() - 1, w / 2, menuFont.getHeight() + 1);
        g.setColor(menuColor[menuStackPtr]);
        g.setFont(menuFont);
        int titleStackPtr = menuStackPtr;
        //while ((menuStack[titleStackPtr].flags & CmdDesc.TITLE_SKIP)!=0)
        // TODO: fix title skip
        //while ((menuStack[titleStackPtr].flags) !=0 )
        //    titleStackPtr--;
        //System.out.println(menuStackPtr);
        String label = menuStack[titleStackPtr].label;
        //System.out.println(label);
        drawLabel(g, label, false, x + 2, y - menuFont.getHeight());
        // Draw 3D menu background
        g.fillRect(x + 2, y + 2, w - 4, h - 4);
        g.setColor((menuColor[menuStackPtr] + 0xfcfcfc) / 2);
        g.fillRect(x, y + 1, 2, h - 1);
        g.setColor(menuColor[menuStackPtr] / 2);
        g.fillRect(x + w - 2, y + 1, 2, h - 1);
        g.setColor((menuColor[menuStackPtr] + 0xfcfcfc) / 2);
        g.fillRect(x, y, w, 1);
        g.fillRect(x + 1, y + 1, w - 2, 1);
        g.setColor(menuColor[menuStackPtr] / 2);
        g.fillRect(x + 2, y + h - 2, w - 4, 1);
        g.fillRect(x + 1, y + h - 1, w - 2, 1);
        // Draw menu items
        g.setColor(0);
        BBMenu [] subMenu = (BBMenu[])menuStack[menuStackPtr].subMenu;
        if (subMenu.length >= 1)
            drawMenuItem(g, subMenu[0], x + w / 2, y + 3, TOP);
        if (subMenu.length >= 2)
            drawMenuItem(g, subMenu[1], x + 3, ym, LEFT);
        if (subMenu.length >= 3)
            drawMenuItem(g, subMenu[2], x + w- 3, ym, RIGHT);
        if (subMenu.length >= 4)
            drawMenuItem(g, subMenu[3], x + w / 2, y + h - 3, BOTTOM);
        if (subMenu.length >= 5 && subMenu[4] != null)
            drawMenuItem(g, subMenu[4], x + w / 2, ym, CENTER);
        else {
            // Draw a small "joystick" in the center
            y += h / 2;
            x += w / 2;
            g.setColor(menuColor[menuStackPtr]/4*3);
            g.fillRect(x - 1, y - 10, 3, 21);
            g.fillRect(x - 10, y - 1, 21, 3);
            g.fillArc(x - 5, y - 5, 11, 11, 0, 360);
        }
    }

    public int keyPressed(int key) {
        //super.keyPressed(keyCode);
        int menuIndex = -1;
        if (key == Keypad.getUnaltedChar('2')) {
            // up
            menuIndex = 0;
        } else if (key == Keypad.getUnaltedChar('8')) {
            // down
            menuIndex = 3;
        } else if (key == Keypad.getUnaltedChar('4')) {
            // left
            menuIndex = 1;
        } else if (key == Keypad.getUnaltedChar('6')) {
            // right
            menuIndex = 2;
        }
        if (menuIndex >= 0)
            return menuAction(menuIndex);
        else
            return -1;
    }
    
    protected int navigationMovement(int dx, int dy, int status, int time) {
        //Dialog.inform("dx: " + dx + ", dy: " + dy);
        dx_sum += dx;
        dy_sum += dy;
        
        int menuIndex = -1;
        if (dy_sum < -1) {
            // up
            menuIndex = 0;
        } else if (dy_sum > 1) {
            // down
            menuIndex = 3;
        } else if (dx_sum < -1) {
            // left
            menuIndex = 1;
        } else if (dx_sum > 1) {
            // right
            menuIndex = 2;
        }

        if (menuIndex >= 0) {
            dx_sum = 0;
            dy_sum = 0;
            return menuAction(menuIndex);
        } else
            return -1;
    }

}
