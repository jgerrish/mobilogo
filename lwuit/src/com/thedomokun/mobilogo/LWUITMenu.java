package com.thedomokun.mobilogo;


import com.thedomokun.gui.Graphics;
import com.thedomokun.gui.Font;
import com.thedomokun.gui.LWUITFont;

import com.sun.lwuit.Display;

import com.thedomokun.common.LogoSymbol;
import com.thedomokun.gui.Menu;

/**
 * @author josh
 *
 * Most of this code is from the wonderful Calc project:
 * http://midp-calc.sourceforge.net/Calc.html
 * 
 */
public class LWUITMenu extends Menu {
    private LWUITFont menuFont;
    private Font boldMenuFont;
    private Font smallMenuFont;
    private Font smallBoldMenuFont;
    private Font numberFont;

    //private static final int TOP_LEFT = Graphics.TOP | Graphics.LEFT;

    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
    private static final int CENTER = 4;
    
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
    public LWUITMenu(String l, LWUITMenu [] m) {
        super();
        label = l;
        subMenu = m;
    }

    public void init_fonts() {
        menuFont = (LWUITFont)LWUITFont.createSystemFont(
                           Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
        boldMenuFont = (LWUITFont)LWUITFont.createSystemFont(
        Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);
        smallMenuFont = (LWUITFont)LWUITFont.createSystemFont(
        Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL);
        smallBoldMenuFont = (LWUITFont)LWUITFont.createSystemFont(
        Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL);        
    }

    public LWUITMenu(int c) {
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
        numberFont = LWUITFont.createSystemFont(Font.FACE_PROPORTIONAL,
                                           Font.STYLE_PLAIN,Font.SIZE_SMALL);
        numberWidth = numberFont.charWidth('A');
        numberHeight = numberFont.getHeight();
        sizeChanged(getWidth(g),getHeight(g));
    }

    public static int getBaselinePosition(Font f) {
        // TODO: fix baseline font
        //int b = g.getBaselinePosition(f);
        int b = 0;
        if (b < f.getHeight()/2) // Obviously wrong
            return f.getHeight()*19/22;
        return b;
    }

    private int labelWidth(String label, boolean bold) {
        Font normalFont = bold ? boldMenuFont : menuFont;
        Font smallFont = bold ? smallBoldMenuFont : smallMenuFont;
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
            //g.drawString(label,x,y,TOP_LEFT);
            g.drawString(label, x, y);
            return;
        }
        boolean sub=false,sup=false,overline=false;
        for (int i=0; i<label.length(); i++) {
            char c = label.charAt(i);
            if (sub)
                g.drawChar(c,x,y+normalFont.getHeight()-
                           getBaselinePosition(smallFont));
            else if (sup)
                g.drawChar(c,x,y-1);
            else
                g.drawChar(c,x,y);
            if (overline) {
                g.drawLine(x-1,y,x+font.charWidth(c)-1,y);
                if (bold)
                    g.drawLine(x-1,y+1,x+font.charWidth(c)-1,y+1);
            }
            x += font.charWidth(c);
        }
    }

    private void drawMenuItem(Graphics g, LWUITMenu menu, int x, int y, int anchor)
    {
        // TODO: fix anchor code
        if (menu == null)
            return;
        boolean bold = (menu.subMenu == null) &&
        //    (menu.flags&CmdDesc.SUBMENU_REQUIRED)==0;
              (menu.flags == 0);
        int width = labelWidth(menu.label,bold);
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
        LWUITMenu[] subMenu = (LWUITMenu[])(menuStack[menuStackPtr].subMenu);
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


    public int keyPressed(int keyCode) {
        //super.keyPressed(keyCode);
        int menuIndex = -1;
        switch(Display.getInstance().getGameAction(keyCode)) {
        case Display.GAME_UP:
            menuIndex = 0;
            //disableMenu();
            break;

        case Display.GAME_DOWN:
            menuIndex = 3;
            //disableMenu();
            break;

        case Display.GAME_LEFT:
            menuIndex = 1;
            break;

        case Display.GAME_RIGHT:
            menuIndex = 2;
            break;

        case Display.GAME_FIRE:
            disableMenu();
            break;
        }
        if (menuIndex >= 0)
            return menuAction(menuIndex);
        else
            return -1;
    }
}
