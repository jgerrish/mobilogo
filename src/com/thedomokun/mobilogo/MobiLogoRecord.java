package com.thedomokun.mobilogo;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

public class MobiLogoRecord {
    public int id;
    public String author;
    public String name;
    public String description;
    public String image;

    public void readModrupal(Hashtable h) throws RecordParseException {
        String str;

        // Get the record id
        str = (String)h.get("vid");
        if (str == null)
            throw new RecordParseException("Invalid node id");
        try {
            id = Integer.valueOf(str).intValue();
        } catch (NumberFormatException ex) {
            throw new RecordParseException(ex.toString());
        }

        // Get the author
        str = (String)h.get("name");
        author = str;
        if (str == null)
            throw new RecordParseException("Invalid name");
        
        // Get the name
        str = (String)h.get("title");
        name = str;
        if (name == null)
            throw new RecordParseException("Invalid title");

        // Get the description
        str = (String)h.get("body");
        description = str;
        if (str == null)
            throw new RecordParseException("Invalid body");

        // Get the screenshot
        Vector screenshots = (Vector)h.get("field_screenshot");
        if (screenshots == null)
            throw new RecordParseException("Invalid screenshot");
        Hashtable h2 = (Hashtable)screenshots.elementAt(0);
        str = (String)h2.get("filename");
        image = str;
        if (str == null)
            throw new RecordParseException("Invalid screenshot filename");
    }
    
    public void readText(Enumeration e) throws RecordParseException {
        String str;
        
        // Get the record id
        try {
            str = (String)e.nextElement();
            if (!str.equals("")) {
                try {
                    id = Integer.valueOf(str).intValue();
                } catch (NumberFormatException ex) {
                    throw new RecordParseException(ex.toString());
                }
            }
        } catch (NoSuchElementException ex) {
            throw new RecordParseException(ex.toString());
        }

        // Get the author
        try {
            str = (String)e.nextElement();
            author = str;
        } catch (NoSuchElementException ex) {
            throw new RecordParseException(ex.toString());
        }

        // Get the name
        try {
            str = (String)e.nextElement();
            name = str;
        } catch (NoSuchElementException ex) {
            throw new RecordParseException(ex.toString());
        }

        // Get the description
        try {
            str = (String)e.nextElement();
            description = str;
        } catch (NoSuchElementException ex) {
            throw new RecordParseException(ex.toString());
        }

        // Get the screenshot
        try {
            str = (String)e.nextElement();
            image = str;
        } catch (NoSuchElementException ex) {
            throw new RecordParseException(ex.toString());
        }
    }
    
    public String toString() {
        return new String("author: " + author + ", title: " + name +
                ", description: " + description + ", screenshot: " + image);
    }
}
