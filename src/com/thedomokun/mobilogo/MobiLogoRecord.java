package com.thedomokun.mobilogo;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class MobiLogoRecord {
    public int id;
    public String author;
    public String name;
    public String description;
    public String image;

    public void read(Enumeration e) throws RecordParseException {
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
}
