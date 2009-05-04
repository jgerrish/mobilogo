/*
 *
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thedomokun.gui;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Image;

import com.thedomokun.gui.MIDPFileBrowser;
import com.thedomokun.mobilogo.MIDPMobiLogo;

/**
 * The <code>FileBrowser</code> custom component lets the user list files and
 * directories. It's uses FileConnection Optional Package (JSR 75). The FileConnection
 * Optional Package APIs give J2ME devices access to file systems residing on mobile devices,
 * primarily access to removable storage media such as external memory cards.
 * @author breh
 */

public class MIDPFileBrowser extends Form
       implements javax.microedition.lcdui.CommandListener {
    private static final String[] attrList = { "Read", "Write", "Hidden" };
    private static final String[] typeList = { "Regular File", "Directory" };

    /**
     * Command fired on file selection.
     */
    public static final Command EXIT_COMMAND = new Command("Exit", Command.BACK, 1);

    private Command select = new Command("Select", Command.ITEM, 1);
    private Command creat = new Command("New", Command.ITEM, 2);

    //add delete file functionality
    private Command delete = new Command("Delete", Command.ITEM, 4);
    private Command creatOK = new Command("OK", Command.OK, 1);
    private Command prop = new Command("Properties", Command.ITEM, 3);
    private Command back = new Command("Back", Command.BACK, 3);
    private Command exit = new Command("Exit", Command.EXIT, 4);

    private String currDirName;
    private String currFile;
    private Image dirIcon;
    private Image fileIcon;
    private Image[] iconList;
    private javax.microedition.lcdui.CommandListener commandListener;
    //private ChoiceGroup filelist;
    private TextField nameInput; // Input field for new file name
    private ChoiceGroup typeInput; // Input field for file type (regular/dir)
    private Displayable old_form;
    
    /* special string denotes upper directory */
    private static final String UP_DIRECTORY = "..";

    /* special string that denotes upper directory accessible by this browser.
     * this virtual directory contains all roots.
     */
    private static final String MEGA_ROOT = "/";

    /* separator string as defined by FC specification */
    private static final String SEP_STR = "/";

    /* separator character as defined by FC specification */
    private static final char SEP = '/';

    private Display display;

    private String selectedURL;

    private String filter = null;

    private String title;
    
    private int mode;
    public final static int OPEN_MODE = 1;
    public final static int SAVE_MODE = 2;
    private MIDPMobiLogo midlet;

    /**
     * Creates a new instance of FileBrowser for given <code>Display</code> object.
     * @param display non null display object.
     */
    public MIDPFileBrowser(Display display, MIDPMobiLogo l) {
        //super("", IMPLICIT);
        super("");
        currDirName = MEGA_ROOT;
        this.display = display;
        super.setCommandListener(this);

        //filelist = new ChoiceGroup("Files", ChoiceGroup.EXCLUSIVE);
        //filelist.
        //filelist.setSelectCommand(SELECT_FILE_COMMAND);

        //list = new FileList(display);
        //this.append(list);
        /*
        try {
            dirIcon = Image.createImage("/org/netbeans/microedition/resources/dir.png");
        } catch (IOException e) {
            dirIcon = null;
        }
        try {
            fileIcon = Image.createImage("/org/netbeans/microedition/resources/file.png");
        } catch (IOException e) {
            fileIcon = null;
        }
        */
        dirIcon = null;
        fileIcon = null;
        iconList = new Image[]{fileIcon, dirIcon};
        //this.append(filelist);
        old_form = display.getCurrent();
        midlet = l;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    
    public void display() {
        display.setCurrent(this);
        showDir();        
    }
    
    private void showDir() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    showCurrDir();
                } catch (SecurityException e) {
                    javax.microedition.lcdui.Alert alert =
                        new javax.microedition.lcdui.Alert("Error", "You are not authorized to access the restricted API", null, AlertType.ERROR);
                    alert.setTimeout(2000);
                    display.setCurrent(alert, MIDPFileBrowser.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == select) {
            List curr = (List)d;
            final String currFile = curr.getString(curr.getSelectedIndex());
            setSelectedFileURL("file:///" + currDirName + currFile);
            new Thread(new Runnable() {
                public void run() {
                    if (currFile.endsWith(SEP_STR) || currFile.equals(UP_DIRECTORY)) {
                        traverseDirectory(currFile);
                    } else {
                        // Return file
                        display.setCurrent(old_form);
                        if (mode == OPEN_MODE)
                            midlet.openFileCallback(getSelectedFileURL());
                        else
                            midlet.saveFileCallback(getSelectedFileURL());
                        //showFile(currFile);
                    }
                }
            }).start();
        } else if (c == creat) {
            createFile();
        } else if (c == creatOK) {
            String newName = nameInput.getString();

            if ((newName == null) || newName.equals("")) {
                javax.microedition.lcdui.Alert alert =
                    new javax.microedition.lcdui.Alert("Error!", "File Name is empty. Please provide file name", null,
                        AlertType.ERROR);
                alert.setTimeout(javax.microedition.lcdui.Alert.FOREVER);
                display.setCurrent(alert);
            } else {
                // Create file in a separate thread and disable all commands
                // except for "exit"
                executeCreateFile(newName, typeInput.getSelectedIndex() != 0);
                display.getCurrent().removeCommand(creatOK);
                display.getCurrent().removeCommand(back);
            }
        } else if (c == back) {
            showCurrDir();
        } else if (c == exit) {
            display.setCurrent(old_form);
        }
/*        } else if (c == delete) {
            List curr = (List)d;
            String currFile = curr.getString(curr.getSelectedIndex());
            executeDelete(currFile);
        }
        */
    }
    /**
     * Sets component's title.
     *  @param title component's title.
     */
    public void setTitle(String title) {
        this.title = title;
        super.setTitle(title);
    }
    
    //Starts creatFile with another Thread
    private void executeCreateFile(final String name, final boolean val) {
        new Thread(new Runnable() {
                public void run() {
                    createFile(name, val);
                }
            }).start();
    }

    /**
     * Show file list in the current directory .
     */
    void showCurrDir() {
        Enumeration e;
        FileConnection currDir = null;
        List browser;

        try {
            if (MEGA_ROOT.equals(currDirName)) {
                e = FileSystemRegistry.listRoots();
                browser = new List(currDirName, List.IMPLICIT);
            } else {
                currDir = (FileConnection)Connector.open("file://localhost/" + currDirName);
                e = currDir.list();
                browser = new List(currDirName, List.IMPLICIT);
                // not root - draw UP_DIRECTORY
                browser.append(UP_DIRECTORY, dirIcon);
            }

            while (e.hasMoreElements()) {
                String fileName = (String)e.nextElement();

                if (fileName.charAt(fileName.length() - 1) == SEP) {
                    // This is directory
                    browser.append(fileName, dirIcon);
                } else {
                    // this is regular file
                    browser.append(fileName, fileIcon);
                }
            }
            browser.setSelectCommand(select);

            //Do not allow creating files/directories beside root
            if (!MEGA_ROOT.equals(currDirName)) {
                browser.addCommand(prop);
                browser.addCommand(creat);
                browser.addCommand(delete);
            }

            browser.addCommand(exit);

            browser.setCommandListener(this);

            if (currDir != null) {
                currDir.close();
            }

            display.setCurrent(browser);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void traverseDirectory(String fileName) {
        /* In case of directory just change the current directory
         * and show it
         */
        if (currDirName.equals(MEGA_ROOT)) {
            if (fileName.equals(UP_DIRECTORY)) {
                // can not go up from MEGA_ROOT
                return;
            }

            currDirName = fileName;
        } else if (fileName.equals(UP_DIRECTORY)) {
            // Go up one directory
            int i = currDirName.lastIndexOf(SEP, currDirName.length() - 2);

            if (i != -1) {
                currDirName = currDirName.substring(0, i + 1);
            } else {
                currDirName = MEGA_ROOT;
            }
        } else {
            currDirName = currDirName + fileName;
        }

        showCurrDir();
    }

    private void openDir(String fileName) {
        /* In case of directory just change the current directory
         * and show it
         */
        if (currDirName.equals(MEGA_ROOT)) {
            if (fileName.equals(UP_DIRECTORY)) {
                // can not go up from MEGA_ROOT
                return;
            }
            currDirName = fileName;
        } else if (fileName.equals(UP_DIRECTORY)) {
            // Go up one directory
            // TODO use setFileConnection when implemented
            int i = currDirName.lastIndexOf(SEP, currDirName.length() - 2);
            if (i != -1) {
                currDirName = currDirName.substring(0, i + 1);
            } else {
                currDirName = MEGA_ROOT;
            }
        } else {
            currDirName = currDirName + fileName;
        }
        showDir();
    }

    /**
     * Returns selected file as a <code>FileConnection</code> object.
     * @return non null <code>FileConection</code> object
     */
    public FileConnection getSelectedFile() throws IOException {
        FileConnection fileConnection = (FileConnection) Connector.open(selectedURL);
        return fileConnection;
    }

    /**
     * Returns selected <code>FileURL</code> object.
     * @return non null <code>FileURL</code> object
     */
    public synchronized String getSelectedFileURL() {
        return selectedURL;
    }

    /**
     * Returns selected <code>FileURL</code> object.
     * @return non null <code>FileURL</code> object
     */
    public synchronized void setSelectedFileURL(String url) {
        selectedURL = new String(url);
    }

    /**
     * Sets the file filter.
     * @param filter file filter String object
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    void createFile() {
        Form creator = new Form("New File");
        nameInput = new TextField("Enter Name", null, 256, TextField.ANY);
        typeInput = new ChoiceGroup("Enter File Type", Choice.EXCLUSIVE,
                typeList, iconList);
        creator.append(nameInput);
        creator.append(typeInput);
        creator.addCommand(creatOK);
        creator.addCommand(back);
        creator.addCommand(exit);
        creator.setCommandListener(this);
        display.setCurrent(creator);
    }

    void createFile(String newName, boolean isDirectory) {
        try {
            FileConnection fc = (FileConnection)Connector.open("file:///" + currDirName + newName);

            if (isDirectory) {
                fc.mkdir();
            } else {
                fc.create();
            }

            showCurrDir();
        } catch (Exception e) {
            String s = "Can not create file '" + newName + "'";

            if ((e.getMessage() != null) && (e.getMessage().length() > 0)) {
                s += ("\n" + e);
            }

            javax.microedition.lcdui.Alert alert =
                new javax.microedition.lcdui.Alert("Error!", s, null, AlertType.ERROR);
            alert.setTimeout(javax.microedition.lcdui.Alert.FOREVER);
            display.setCurrent(alert);
            // Restore the commands that were removed in commandAction()
            display.getCurrent().addCommand(creatOK);
            display.getCurrent().addCommand(back);
        }
    }
    
    
}
