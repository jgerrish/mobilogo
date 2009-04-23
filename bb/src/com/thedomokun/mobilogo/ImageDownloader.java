package com.thedomokun.mobilogo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;

import net.rim.device.api.system.Bitmap;

import com.thedomokun.gui.BBImage;
import com.thedomokun.gui.Image;

public class ImageDownloader {
    Image img = null;

    /**
     * Download an image from the server
     * 
     * @param url the URL of the image
     * @return the image
     * @throws IOException
     */
    public Image getImage(String url) throws IOException {
        ContentConnection connection = null;
        try {
            connection = (ContentConnection)Connector.open(url);
        } catch (IOException e) {
            if (connection != null)
                connection.close();
            throw e;
        }
        // * There is a bug in MIDP 1.0.3 in which read() sometimes returns
        //   an invalid length. To work around this, I have changed the 
        //   stream to DataInputStream and called readFully() instead of read()
        //     InputStream iStrm = connection.openInputStream();
        DataInputStream iStrm = connection.openDataInputStream();
        ByteArrayOutputStream bStrm = null;
        Image im;

        try {
            // ContentConnection includes a length method
            byte imageData[];
            int length = (int)connection.getLength();
            if (length != -1) {
                imageData = new byte[length];
                // Read the png into an array
                // iStrm.read(imageData);        
                iStrm.readFully(imageData);
            } else { // Length not available...
                bStrm = new ByteArrayOutputStream();
                int ch;
                while ((ch = iStrm.read()) != -1)
                    bStrm.write(ch);
                    imageData = bStrm.toByteArray();
                    bStrm.close();                
                }

            // Create the image from the byte array
            im = new BBImage(Bitmap.createBitmapFromPNG(imageData, 0, imageData.length));
        } finally {
            // Clean up
            if (iStrm != null)
                iStrm.close();
            if (bStrm != null)
                bStrm.close();                              
        }
        if (connection != null)
            connection.close();
        return (im == null ? null : im);
    }

    public ImageDownloader() {
    }
  
    public ImageDownloader(String url) {
        try {
            img = getImage(url);
        } catch (Exception e) {
            System.err.println("Msg: " + e.toString());
        }
    }

}