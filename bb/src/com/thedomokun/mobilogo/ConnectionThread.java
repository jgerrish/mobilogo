package com.thedomokun.mobilogo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.thedomokun.gui.BBImage;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

/**
 * The ConnectionThread class manages the HTTP connection. Fetch operations
 * are not queued, but if a second fetch request is made while a previous
 * request is still active, the second request stalls until the previous
 * request completes.
 */
public class ConnectionThread extends Thread {
    private Vector scripts = null;
    private BBProjectListField listField = null;

    private static final int TIMEOUT = 500; // ms

    private String _theUrl;

    private volatile boolean _start = false;
    private volatile boolean _stop = false;

    public ConnectionThread(BBProjectListField field) {
        this.listField = field;
    }
    
    // retrieve the URL
    public synchronized String getUrl() {
        return _theUrl;
    }

    // fetch a page and set the _start variable
    public void fetch(String url) {
        _start = true;
        _theUrl = url;
    }

    // close the thread and set the _stop variable
    public void stop() {
        _stop = true;
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
                StreamConnection s = null;
                try {
                    s = (StreamConnection)Connector.open(getUrl());
                    try {
                        InputStream input = s.openInputStream();

                        byte[] data = new byte[256];
                        int len = 0;
                        StringBuffer raw = new StringBuffer();
                        while (-1 != (len = input.read(data))) {
                            raw.append(new String(data, 0, len));
                        }

                        // convert raw data to String format
                        final String text = raw.toString();

                        // invoke updateContent method to display retrieved text
                        updateContent(text);

                        // close the input stream
                        input.close();

                        // close the stream connection
                        s.close();
                    } catch (IOException e) {
                        final String error = e.toString();
                        UiApplication.getUiApplication().invokeLater(new Runnable() {
                            public void run() {
                                Dialog.alert(error);
                            }
                        });
                        System.err.println(e.toString());
                        s.close(); // caught by surrounding catch
                    }
                } catch (IOException e) {
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

    // TODO: Refactor this method
    private void updateContent(final String text) {
        MobiLogoRecords records = new MobiLogoRecords();
        ImageDownloader img_conn = new ImageDownloader();
        try {
            records.parse(text);
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
        String base_url = BBMobiLogoBrowseScreen.base_url;
        for (int i = 0; i < num_records; i++) {
            BBImage img = null;
            MobiLogoRecord record = (MobiLogoRecord)records.records.elementAt(i);
            titles[i] = record.name;
            authors[i] = record.author;
            descriptions[i] = record.description;

            StringBuffer url = new StringBuffer();
            url.append(base_url);
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
                //Dialog.inform();
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
