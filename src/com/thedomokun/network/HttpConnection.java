package com.thedomokun.network;

//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;

public class HttpConnection {
    javax.microedition.io.HttpConnection conn = null;

    public HttpConnection() {
    }

    public Vector getScripts(String url) {
        Vector scripts = null;
        //DataInputStream dis = null;
        //DataOutputStream dos = null;      

        try {
            conn = (javax.microedition.io.HttpConnection)Connector.open(url);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        try {
            conn.setRequestMethod(javax.microedition.io.HttpConnection.GET);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                conn.close();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            e.printStackTrace();
            return null;
        }
        
        try {
            conn.setRequestProperty("User-Agent", "BlackBerry/4.6.1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                conn.close();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            return null;
        }

        try {
            conn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //String lang = conn.getRequestProperty("Content-Language");
        
        return scripts;
    }
}
