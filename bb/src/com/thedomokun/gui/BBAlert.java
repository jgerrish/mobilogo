package com.thedomokun.gui;

public class BBAlert extends Alert {

    //@Override
    public void alert(String str) {
        net.rim.device.api.ui.component.Dialog.alert(str);
    }

    //@Override
    public void alert(String str, String str2) {
        net.rim.device.api.ui.component.Dialog.alert(str);
    }

}
