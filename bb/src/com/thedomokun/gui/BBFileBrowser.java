/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package com.thedomokun.gui;

import java.util.*;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.FullScreen;


public class BBFileBrowser extends FullScreen {
    private String currDirName;
    private Image dirIcon;
    private Image fileIcon;
    private Image[] iconList;
    private FileList listfield;
    private LabelField filelabel;
    private EditField editfield;
    private final int font_height = Font.getDefault().getHeight();
    
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

    private String selectedURL;

    private String filter = null;

    private LabelField titleField;
    
    /**
     * Creates a new instance of FileBrowser for given <code>Display</code> object.
     * @param display non null display object.
     */
    public BBFileBrowser() {
        super(NO_VERTICAL_SCROLL | DEFAULT_MENU | DEFAULT_CLOSE);

        titleField = new LabelField("MobiLogo");
        this.add(titleField);

        
        currDirName = MEGA_ROOT;
        /*
        try {
            dirIcon = Image.createImage("/org/netbeans/microedition/resources/dir.png");
        } catch (IOException e) {
            dirIcon = null;
        }
        */
        dirIcon = null;
        /*
        try {
            fileIcon = Image.createImage("/org/netbeans/microedition/resources/file.png");
        } catch (IOException e) {
            fileIcon = null;
        }
        */
        fileIcon = null;
        iconList = new Image[]{fileIcon, dirIcon};

        listfield = new FileList();
        //callback = new ListCallback();
        //listfield.setCallback(callback);

        //Dialog.inform("first test");
        this.add(listfield);

        SeparatorField sep = new SeparatorField();
        this.add(sep);
        filelabel = new LabelField("File name: ");
        this.add(filelabel);
        editfield = new EditField();
        this.add(editfield);

        showDir();
    }

    private void showDir() {
        try {
            showCurrDir();
        } catch (SecurityException e) {
            final String str = e.toString();
            UiApplication.getUiApplication().invokeLater(new Runnable(){
                public void run() {
                    Dialog.inform(str);
                }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        new Thread(new Runnable() {

            public void run() {
                try {
                    showCurrDir();
                } catch (SecurityException e) {
                    Dialog.alert("You are not authorized to access the restricted API");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
    }

    /**
     * Sets component's title.
     *  @param title component's title.
     */
    public void setTitle(String title) {
        this.titleField.setText(title);
    }

    /**
     * Show file list in the current directory .
     */
    private void showCurrDir() {
        int cnt = 0;
        listfield.clear();

        if (!currDirName.equals(titleField.getText()))
            setTitle(currDirName);

        Enumeration e = null;
        FileConnection currDir = null;
        if (MEGA_ROOT.equals(currDirName)) {
            listfield.insert(cnt++, UP_DIRECTORY);
            e = FileSystemRegistry.listRoots();
        } else {
            try {
                currDir = (FileConnection) Connector.open("file:///" +
                        currDirName);
                e = currDir.list();
            } catch (IOException ioe) {
                final String str = ioe.toString();
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        Dialog.inform(str);
                    }
                });

            }
            listfield.insert(cnt++, UP_DIRECTORY);
        }

        if (e == null) {
            try {
                currDir.close();
            } catch (IOException ioe) {
                final String str = ioe.toString();
                UiApplication.getUiApplication().invokeLater(new Runnable(){
                    public void run() {
                        Dialog.inform(str);
                    }
                    });

                ioe.printStackTrace();
            }
            invalidate();
            return;
        }

        while (e.hasMoreElements()) {
            String fileName = (String) e.nextElement();
            if (fileName.charAt(fileName.length() - 1) == SEP) {
                // This is directory
                listfield.insert(cnt++, fileName);
            } else {
                // this is regular file
                if (filter == null || fileName.indexOf(filter) > -1) {
                    listfield.insert(cnt++, fileName);
                }
            }
        }

        if (currDir != null) {
            try {
                currDir.close();
            } catch (IOException ioe) {
                final String str = ioe.toString();
                UiApplication.getUiApplication().invokeLater(new Runnable(){
                    public void run() {
                        Dialog.inform(str);
                    }
                    });
                ioe.printStackTrace();
            }
        }
        invalidate();
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
    public String getSelectedFileURL() {
        selectedURL = "file:///" + currDirName +
                      BBFileBrowser.this.editfield.getText();
        return selectedURL;
    }

    /**
     * Sets the file filter.
     * @param filter file filter String object
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    protected void sublayout(int width, int height) {
        super.sublayout(width, height);
    }
    
    private class FileList extends ObjectListField {
        /*
        protected void layout(int width, int height) {
            height = height - (font_height * 3) - 10;
            //Dialog.inform("height: " + height);
            setExtent(width, height);
            //super.layout(width, height);
        }
        */
        
        protected int moveFocus(int amount, int status, int time) {
            int amt = super.moveFocus(amount, status, time);
            int sel = getSelectedIndex();
            BBFileBrowser.this.editfield.setText((String)get(this, sel));

            return amt;
        }
        
        protected boolean navigationClick(int status, int time) {
            pickItem();
            return true;
        }

        protected void pickItem() {
            int sel = getSelectedIndex();
            String currFile = (String)get(this, sel);
            if (currFile.endsWith(SEP_STR) ||
                    currFile.equals(UP_DIRECTORY)) {
                openDir(currFile);
            } else {
                //switch To Next
                //selectedURL = "file:///" + currDirName + currFile;
                selectedURL = "file:///" + currDirName +
                              BBFileBrowser.this.editfield.getText();
                UiApplication.getUiApplication().popScreen(BBFileBrowser.this);
            }
        }
        
        /**
         * @see net.rim.device.api.ui.component.ListFieldCallback#drawListRow(ListField , Graphics , int , int , int)
         */
        public void drawListRow(ObjectListField listField, Graphics graphics,
                int index, int y, int width) {
            if (index < getSize()) {
                String text = (String)this.get(index);;
                graphics.drawText(text, 0, y);
            }
        }

       /**
        * @see net.rim.device.api.ui.component.ListFieldCallback#getPreferredWidth(ListField)
        */
        public int getPreferredWidth(ObjectListField listField) {
            return Display.getWidth();
        }

        /**
         * Allows space bar to page down.
         * 
         * @see net.rim.device.api.ui.Screen#keyChar(char , int , int)
         */
        public boolean keyChar(char key, int status, int time) {
            if (getSize() > 0 && key == Characters.SPACE) {
                getScreen().scroll(Manager.DOWNWARD);
                return true;
            }
            if ((getSize() > 0) && (key == Characters.ENTER))
                pickItem();
            
            return super.keyChar(key, status, time);
        }

        /**
         * Removes all elements from this list field.
         */
        void clear() {
            int size = getSize();
            for (int i = 0; i < size; i++) {
                delete(0);
            }
        }
    }
}
