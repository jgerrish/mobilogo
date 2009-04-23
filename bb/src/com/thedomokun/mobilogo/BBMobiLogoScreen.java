/**
 * 
 */
package com.thedomokun.mobilogo;

import java.io.IOException;

import com.thedomokun.gui.BBFileBrowser;

import net.rim.device.api.i18n.HashResourceBundle;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

/**
 * @author Joshua Gerrish
 *
 */
public class BBMobiLogoScreen extends MainScreen {
    private HashResourceBundle _resources;
    private MenuItem menuItemOpen, menuItemSave, menuItemBrowse, menuItemShare;
    // TODO: Setup proper accessors and owners of the components
    public BBMobiLogoCanvas logoComponent;

    /**
     * @param style
     */
    public BBMobiLogoScreen(long style) {
        super(style);
        _resources = new HashResourceBundle(Locale.getDefault());
        _resources.put(1, new String("Open"));
        menuItemOpen = new MenuItem(_resources, 1, 200000, 10) {
            public void run() {
                onOpenCommand();
            }
        };            
        _resources.put(2, new String("Save"));
        menuItemSave = new MenuItem(_resources, 2, 200000, 10) {
            public void run() {
                onSaveCommand();
            }
        };
        
        _resources.put(3, new String("Browse"));
        menuItemBrowse = new MenuItem(_resources, 3, 200000, 10) {
            public void run() {
                onBrowseCommand();
            }
        };

        _resources.put(4, new String("Share"));
        menuItemShare = new MenuItem(_resources, 4, 200000, 10) {
            public void run() {
                onShareCommand();
            }
        };

    }

    protected void makeMenu(Menu menu, int instance) {
        super.makeMenu(menu, instance);
        /*
        Field focus =
        UiApplication.getUiApplication().getActiveScreen().getLeafFieldWithFocus();
        if (focus != null) {
            ContextMenu contextMenu = focus.getContextMenu();
            if (!contextMenu.isEmpty()) {
                menu.add(contextMenu);
                menu.addSeparator();
            }
        }
        */
        menu.addSeparator();
        menu.add(menuItemOpen);
        menu.add(menuItemSave);
        menu.add(menuItemBrowse);
        menu.add(menuItemShare);
    }
    
    public void makeContextMenu(ContextMenu contextMenu) {
        contextMenu.addItem(menuItemOpen);
        contextMenu.addItem(menuItemSave);
        contextMenu.addItem(menuItemBrowse);
        contextMenu.addItem(menuItemShare);
      }

    private void onOpenCommand() {
        BBFileBrowser fb = new BBFileBrowser();
        UiApplication.getUiApplication().pushModalScreen(fb);
        String fn = fb.getSelectedFileURL();
        if (fn == null)
            return;
        //Dialog.inform(fn);
        /*
        UiApplication.getUiApplication().invokeLater(new Runnable(){
            public void run() {
                Dialog.inform(str);
            }
        });
        */
        MobiLogoFile f = new MobiLogoFile();
        try {
            String src = f.loadFile(fn);
            //Dialog.inform("file: " + fn + ", src: " + src);
            if (logoComponent != null) {
                logoComponent.setSource(src);
                logoComponent.interpret(true);
            } else {
                Dialog.alert("No canvas set");
            }
        } catch (IOException e) {
            Dialog.alert(e.toString());
        }
    }

    private void onSaveCommand() {
        BBFileBrowser fb = new BBFileBrowser();

        UiApplication.getUiApplication().pushModalScreen(fb);
        final String fn = fb.getSelectedFileURL();
        if (fn == null)
            return;

        MobiLogoFile f = new MobiLogoFile();
        try {
            Dialog.alert(logoComponent.source.getText());
            f.saveFile(fn, logoComponent.source.getText());
        } catch (IOException e) {
            Dialog.alert(e.toString());    
        }

        /*
        UiApplication.getUiApplication().invokeLater(new Runnable(){
            public void run() {
                Dialog.inform(str);
            }
        });
        */
    }

    private void onBrowseCommand() {
        BBMobiLogoBrowseScreen bs = new BBMobiLogoBrowseScreen();
        UiApplication.getUiApplication().pushModalScreen(bs);
        bs._connectionThread.stop();
    }
    
    private void onShareCommand() {
        BBMobiLogoBrowseScreen bs = new BBMobiLogoBrowseScreen();
        UiApplication.getUiApplication().pushModalScreen(bs);
        bs._connectionThread.stop();
    }

    public boolean navigationClick(int status, int time) {
        if ((status & KeypadListener.STATUS_TRACKWHEEL) ==
            KeypadListener.STATUS_TRACKWHEEL) {
        //Input came from the trackwheel
        } else if ((status & KeypadListener.STATUS_FOUR_WAY) ==
                   KeypadListener.STATUS_FOUR_WAY) {
        //Input came from a four way navigation input device
        }
        return super.navigationClick(status, time);
    }
}
