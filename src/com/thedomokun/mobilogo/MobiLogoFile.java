/**
 * 
 */
package com.thedomokun.mobilogo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

//import com.thedomokun.gui.BBDialog;

/**
 * @author Joshua Gerrish
 *
 */
public class MobiLogoFile {
    FileConnection fc;
    String dir = "file:///SDCard/mobilogo/";
    String start_file = "start.logo";

    public MobiLogoFile() {
    }

    public void createInitialStructure() {
        // Open Media Card directory or make it
        String fn = "file:///SDCard/";
        try {
            fc = (FileConnection)Connector.open(fn);
            if (!fc.exists()) {
                fc.mkdir();
            }
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
/*
        } catch (ControlledAccessException e) {
            final String str = "Error accessing SD card.  You should enable the Blackberry Firewall in Options.";
            UiApplication.getUiApplication().invokeLater(new Runnable(){
                public void run() {
                    Dialog.inform(str);
                }
                });
            return;
*/
        }

        // Open mobilogo subdirectory or make it
        fn = "file:///SDCard/mobilogo/";
        try {
            fc = (FileConnection)Connector.open(fn);
            if (!fc.exists())
                fc.mkdir();
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String readFile() throws Exception {
        // Open start file or make it
        String fn = dir + start_file;

        String test = "TO SQUARE REPEAT 4 [ FD 30 RT 90 ] END\nSQUARE";

        try {
            fc = (FileConnection)Connector.open(fn);
            if (!fc.exists()) {
                fc.create();
                OutputStream os = fc.openOutputStream();
                os.write(test.getBytes());
                os.close();
            } else {
                // Read file
                byte[] b = new byte[1024];
                try {
                    InputStream is = fc.openInputStream();
                    int length = is.read(b, 0, 1024);
                    is.close();
                    test = new String(b, 0, length);
                } catch (Exception e) {
                    throw(e);
                }
            }
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return test;
    }
    
    public String loadFile(String filename) throws IOException {
        try {
            FileConnection fc = (FileConnection)Connector.open(filename);
            if(!fc.exists()) {
                throw new IOException("File does not exist");
            }
            InputStream is = fc.openInputStream();
            byte b[] = new byte[1024];
            int length = is.read(b, 0, 1024);
            is.close();
            fc.close();
            return new String(b, 0, length);
        } catch (IOException e) {
            throw e;
        }
    }

    public void saveFile(String filename, String content) throws IOException {
        try {
            FileConnection fc = (FileConnection)Connector.open(filename);
            if(fc.exists()) {
                fc.delete();
                fc.close();
                fc = (FileConnection)Connector.open(filename);
            }
            fc.create();
            if (!fc.canWrite()) {
                //BBDialog.alert("Can't write to file!");
                return;
            }
            OutputStream os = fc.openOutputStream();
            //DataOutputStream ds = fc.openDataOutputStream();
            byte b[] = content.getBytes();
            //Dialog.inform("len: " + b);
            os.write(b);
            //os.write(content.getBytes(), );
            os.flush();
            os.close();
            fc.close();
        } catch (Exception e) {
            //BBDialog.alert(e.toString());
        }
    }

}
