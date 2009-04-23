/**
 * 
 */
package com.thedomokun.mobilogo;

import java.util.Vector;

import com.thedomokun.network.HttpConnection;

import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.container.FullScreen;


/**
 * @author Joshua Gerrish
 *
 */
public class BBMobiLogoBrowseScreen extends FullScreen {
    public BBProjectListField scriptList;
    private Vector scripts;
    // define a constant for the location of the page to retrieve
    public static final String base_url = "http://www.thedomokun.com/~josh/mobilogo/";
    private static final String script_url = base_url + "getscripts.php";

    // define a constant for the valid http protocol strings
    private static final String[] HTTP_PROTOCOL = { "http://", "http:\\" };


    // create a new ConnectionThread
    public ConnectionThread _connectionThread;

    public BBMobiLogoBrowseScreen() {
        super(DEFAULT_MENU | DEFAULT_CLOSE);
        scriptList = new BBProjectListField();
        scriptList.setScreen(this);
        // Set the row height to 3 times the font height
        scriptList.setRowHeight(52);
        add(scriptList);
        
        _connectionThread = new ConnectionThread(scriptList);

        _connectionThread.start();
        //HttpConnection conn = new HttpConnection();
        //scripts = conn.getScripts("http://oscar/~josh/getscripts.php");
        //String s[] = new String[scripts.size()];
        //scripts.copyInto(s);
        fetchPage(script_url);
        //scriptList.set(s);
    }

    // methods
    private void fetchPage(String url) {
        // normalize the URL
        String lcase = url.toLowerCase();
        boolean validHeader = false;
        int i = 0;

        // winding down and comparing to 0 saves instructions
        for (i = HTTP_PROTOCOL.length - 1; i >= 0; --i) {
            if (-1 != lcase.indexOf(HTTP_PROTOCOL[i])) {
                validHeader = true;
                break;
            }
        }
        if (!validHeader) {
            url = HTTP_PROTOCOL[0] + url; // prepend the protocol specifier
        }

        // create a new thread for connection operations
        _connectionThread.fetch(url);
    }


}
