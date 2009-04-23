/**
 * 
 */
package com.thedomokun.mobilogo;

import net.rim.device.api.i18n.HashResourceBundle;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.EditField;

/**
 * @author Joshua Gerrish
 *
 */
public class BBSourceField extends EditField {
    public BBMobiLogoCanvas canvas;
    private HashResourceBundle _resources;
    private MenuItem myContextMenuItemA;

    /**
     * TODO: Use actual resource files, not HashResourceBundle
     */
    public BBSourceField(String a, String b, BBMobiLogoCanvas c) {
        super(a, b);
        canvas = c;

        _resources = new HashResourceBundle(Locale.getDefault());
        _resources.put(1, new String("Run"));        
        myContextMenuItemA = new MenuItem(
                _resources, 1, 200000, 10) {
        
            public void run() {
                onSaveCommand();
            }
        };
    }

    private void onSaveCommand() {
        canvas.setSource(getText());
        canvas.interpret(true);
    }

    
    protected void makeContextMenu(ContextMenu contextMenu) {
        contextMenu.addItem(myContextMenuItemA);
        //contextMenu.addItem(myContextMenuItemB);
    }

}
