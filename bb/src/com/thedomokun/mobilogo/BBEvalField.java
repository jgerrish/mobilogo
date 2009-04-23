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
public class BBEvalField extends EditField {
    public BBMobiLogoCanvas canvas;
    private HashResourceBundle _resources;
    private MenuItem evalMenuItem;

    /**
     * TODO: Use actual resource files, not HashResourceBundle
     */
    public BBEvalField(String a, String b, BBMobiLogoCanvas c) {
        super(a, b);
        canvas = c;

        _resources = new HashResourceBundle(Locale.getDefault());
        _resources.put(1, new String("Eval"));        
        evalMenuItem = new MenuItem(
                _resources, 1, 200000, 10) {
        
            public void run() {
                onEvalCommand();
            }
        };
    }

    private void onEvalCommand() {
        StringBuffer buf = new StringBuffer();
        buf.append(canvas.getSource());
        buf.append("\n");
        buf.append(this.getText());
        canvas.setSource(buf.toString());
        canvas.interpret(true);
    }

    
    protected void makeContextMenu(ContextMenu contextMenu) {
        contextMenu.addItem(evalMenuItem);
    }

}
