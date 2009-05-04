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
    public static final String url =
        "http://www.thedomokun.com/drupal/?q=services/xmlrpc";
    public static final String image_base_url =
        "http://www.thedomokun.com/drupal/sites/default/files/";
    
    // create a new ConnectionThread
    public ModrupalThread modrupalThread;

    public BBMobiLogoBrowseScreen() {
        super(DEFAULT_MENU | DEFAULT_CLOSE);
        scriptList = new BBProjectListField();
        scriptList.setScreen(this);
        // Set the row height to 3 times the font height
        scriptList.setRowHeight(52);
        add(scriptList);
        
        modrupalThread = new ModrupalThread(scriptList);

        modrupalThread.start();
        fetchPage(url);
        //scriptList.set(s);
    }

    // methods
    private void fetchPage(String url) {
        // normalize the URL
        // create a new thread for connection operations
        modrupalThread.fetch(url);
    }


}
