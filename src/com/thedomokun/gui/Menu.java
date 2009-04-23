/**
 * 
 */
package com.thedomokun.gui;


/**
 * @author josh
 *
 */
public abstract class Menu {
    protected int numRepaintLines;

    protected int offX, offY, nDigits, nLines, numberWidth, numberHeight;
    protected int offY2, offYMonitor, nLinesMonitor;

    protected int menuX,menuY,menuW,menuH;
    protected int header,footer;

    protected Menu[] menuStack;
    protected int menuStackPtr;
    protected int menuCommand;
    protected Menu menuItem;
    protected Menu repeatedMenuItem;
    protected boolean menuEnabled;
    
    // current menu
    protected int currentMenu;

    /* From the menu inner class */
    public String label;
    public int command;
    //public int returnCommand;
    public byte flags;
    public Menu[] subMenu;

    public Menu() {
        init_fonts();
        menuStack = new Menu[4]; // One too many, I think
        menuStackPtr = -1;
    }

    public Menu(String l, int c) {
        this();
        label = l;
        command = c;
    }

    public Menu(String l, int c, int f) {
        label = l;
        command = c;
        flags = (byte)f;
    }

    public Menu(String l, Menu [] m) {
        this();
        label = l;
        subMenu = m;
    }

    public Menu(String l, int f, Menu [] m) {
        label = l;
        flags = (byte)f;
        subMenu = m;
    }

    protected abstract void init_fonts();

    public void disableMenu() {
        menuEnabled = false;
    }

    public void enableMenu() {
        menuEnabled = true;
    }

    public boolean menuEnabled() {
        return menuEnabled;
    }

    
    public void setMenu(int i, Menu menu) {
        menuStackPtr = i;
    }
    
    public void resetMenu() {
        // Open top level menu
        menuStack[0] = this;
        menuStackPtr = 0;
        numRepaintLines = 0; // Force repaint of menu
    }

    
    protected boolean plainLabel(String label) {
        return true;
    }


    
    protected int menuAction(int menuIndex) throws OutOfMemoryError {
        int returnCommand = -1;
        //System.out.println("menuStackPtr: " + menuStackPtr + ", menuIndex: " + menuIndex);

        if (menuStackPtr < 0 && menuIndex < 4) {
            // Go directly to submenu
            menuStack[0] = this;
            menuStack[1] = this.subMenu[menuIndex];
            menuStackPtr = 1;
            numRepaintLines = 0; // Force repaint of menu
        } else if (menuStackPtr < 0) {
            // Open top level menu
            menuStack[0] = this;
            menuStackPtr = 0;
            numRepaintLines = 0; // Force repaint of menu
        } else if (menuIndex < menuStack[menuStackPtr].subMenu.length) {
            Menu subItem = menuStack[menuStackPtr].subMenu[menuIndex];
            if (subItem == null) {
                ; // NOP
            } else if (subItem.subMenu != null) {
                // Open submenu
                menuStackPtr++;
                menuStack[menuStackPtr] = subItem;
                // Set correct labels
                /*
                if (subItem == progMenu.subMenu[4])
                    for (int i=0; i<5; i++)
                        progMenu.subMenu[4].subMenu[i].label =
                            calc.progLabels[i+4];
                */
                numRepaintLines = 0; // Force repaint of menu
            } else {
                //System.out.println("Command found");
                returnCommand = subItem.command;
                // Handle commands

                disableMenu();
                menuStackPtr = -1;     // Remove menu
                numRepaintLines = 100; // Force repaint of all
            }
        }

        return returnCommand;
    }

    public void drawMenu(Graphics g) {
    }
}
