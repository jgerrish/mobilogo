package com.thedomokun.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

public class MIDPAlert extends com.thedomokun.gui.Alert {
    private Display display;

    public MIDPAlert(Display d) {
        this.display = d;
    }
    
    public void alert(String str) {
        Alert alert =
            new Alert("Error", str, null, AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        display.setCurrent(alert);
    }

    public void alert(String str, String str2) {
        Alert alert =
            new Alert("Error", str, null, AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        display.setCurrent(alert);
    }
}
