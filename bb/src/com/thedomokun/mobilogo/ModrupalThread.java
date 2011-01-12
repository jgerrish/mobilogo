/**
 * 
 */
package com.thedomokun.mobilogo;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.itsdarkhere.modrupal.ModrupalServiceClient;
import com.itsdarkhere.modrupal.services.SystemService;
import com.itsdarkhere.modrupal.services.TaxonomyService;
import com.itsdarkhere.modrupal.services.UserService;
import com.thedomokun.gui.BBImage;

/**
 * @author Joshua Gerrish
 *
 */
public class ModrupalThread extends Thread {
    private ModrupalServiceClient client = null;
    private volatile boolean _start = false;
    private volatile boolean _stop = false;

    private BBProjectListField listField = null;
    private static final int TIMEOUT = 500; // ms
    private String url = null;

    public ModrupalThread(BBProjectListField field) {
        String url = "url";
        String api_key = "api_key";
        String domain = "domain";

        client = new ModrupalServiceClient(url, api_key, domain);
        this.listField = field;
    }

    // fetch a page and set the _start variable
    public void fetch(String url) {
        this.url = url;
        _start = true;
    }

    public Vector getContent() {
        String username = "josh";
        String password = "mpwpwd";

        SystemService system = new SystemService(client);
        Hashtable res = system.connect();
        String sessid = (String)res.get("sessid");
        client.setSession(sessid);

        if (sessid == null) {
            System.out.println("Error getting session id");
            return null;
        }

        Hashtable userinfo = (Hashtable)res.get("user");
        if (userinfo != null) {
            String name = (String)userinfo.get("name");
            if (name != null)
                if (name.equals("josh")) {
                    // already logged in            
                } else {
                    UserService user = new UserService(client);
                    Hashtable session = null;
                    session = user.login(username, password);
                    sessid = (String)session.get("sessid");
                    if (sessid != null)
                        client.setSession(sessid);
                    else
                        System.out.println("Invalid session id from user.login");
                }
        }

        //System.out.println("user.login: " + session);

        TaxonomyService taxonomy = new TaxonomyService(client);
        Vector nodes = null;

        Vector terms = new Vector();
        terms.addElement(new Integer(1));

        Vector fields = new Vector();
        fields.addElement(new String("vid"));
        fields.addElement(new String("title"));
        fields.addElement(new String("name"));
        fields.addElement(new String("body"));
        fields.addElement(new String("field_screenshot"));
        //fields.addElement(new String("field_sourcecode"));

        nodes = taxonomy.selectNodes(terms, fields);
        //System.out.println("nodes: " + nodes);

        return nodes;
    }

    public void run() {
        for (;;) {
            // thread control
            while (!_start && !_stop) {
                try {
                    sleep(TIMEOUT);
                } catch (InterruptedException e) {
                    final String s = e.toString();
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            Dialog.inform(s);
                        }
                    });

                    System.err.println(e.toString());
                }
            }
            // exit condition
            if (_stop) {
                return;
            }
            synchronized (this) {
                // open the connection and extract the data
                try {
                    Vector nodes = getContent();
                    // invoke updateContent method to display retrieved text
                    updateContent(nodes);
                } catch (Exception e) {
                    final String error = e.toString();
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            Dialog.alert(error);
                        }
                    });
                    System.err.println(e.toString());
                }
                // reset the start state
                _start = false;
            }
        }
    }

    public void stop() {
        _stop = true;        
    }

    // TODO: Refactor this method
    private void updateContent(final Vector nodes) {
        MobiLogoRecords records = new MobiLogoRecords();
        ImageDownloader img_conn = new ImageDownloader();
        try {
            records.parseModrupal(nodes);
        } catch (RecordParseException e) {
            final String str2 = e.toString();
            UiApplication.getUiApplication().invokeLater(new Runnable(){
                public void run() {
                    Dialog.inform(str2);
                }
                });
            return;
        }
        int num_records = records.size();

        String authors[] = new String[num_records];
        String titles[] = new String[num_records];
        String descriptions[] = new String[num_records];
        BBImage images[] = new BBImage[num_records];

        // We now have the records from the server.  Get the images
        String image_base_url = BBMobiLogoBrowseScreen.image_base_url;
        for (int i = 0; i < num_records; i++) {
            BBImage img = null;
            MobiLogoRecord record = (MobiLogoRecord)records.records.elementAt(i);
            titles[i] = record.name;
            authors[i] = record.author;
            descriptions[i] = record.description;

            StringBuffer url = new StringBuffer();
            url.append(image_base_url);
            url.append(record.image);
            try {
                img = (BBImage)img_conn.getImage(url.toString());
                images[i] = img;
            } catch (Exception e) {
                System.err.println("Msg: " + e.toString());
            }
        }

        final String a2[] = authors;
        final String s2[] = titles;
        final String d2[] = descriptions;
        final BBImage i2[] = images;

        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                //Dialog.inform("Adding items");
                if (listField != null)
                    listField.set(s2);

                // Add the images to the list field
                for (int i = 0; i < i2.length; i++) {
                    listField.addImage(i2[i].image);
                    listField.addAuthor(a2[i]);
                    listField.addDescription(d2[i]);
                }
            }
        });

        //scripts.addElement(text);
    }

}
