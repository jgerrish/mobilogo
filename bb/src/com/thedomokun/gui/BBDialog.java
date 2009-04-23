/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author Joshua Gerrish
 *
 */
public class BBDialog extends Dialog {

    /**
     * 
     */
    public BBDialog() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.thedomokun.gui.Dialog#alert(java.lang.String)
     */
    //@Override
    public static void alert(String str) {
        // TODO Auto-generated method stub
        net.rim.device.api.ui.component.Dialog.alert(str);
    }

    /* (non-Javadoc)
     * @see com.thedomokun.gui.Dialog#inform(java.lang.String)
     */
    //@Override
    public static void inform(String str) {
        // TODO Auto-generated method stub
        net.rim.device.api.ui.component.Dialog.inform(str);
    }

}
