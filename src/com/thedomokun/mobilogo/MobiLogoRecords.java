package com.thedomokun.mobilogo;

import java.util.Enumeration;
import java.util.Vector;

public class MobiLogoRecords {
    public Vector records;
    
    public Vector getRecords() {
        return records;
    }
    
    public int size() {
        return records.size();
    }

    public void parse(String text) throws RecordParseException {
        Vector strings;
        strings = splitString(text);

        records = new Vector();

        int cnt = strings.size();

        // Invalid data.  Each record should contain five elements
        // TODO: Throw an exception
        if ((cnt % 5) != 0)
            return;

        Enumeration e = strings.elements();
        while (e.hasMoreElements()) {
            MobiLogoRecord r = new MobiLogoRecord();
            r.read(e);
            records.addElement(r);
        }
    }

    public Vector splitString(String text) {
        Vector strings = new Vector();
        
        int cur = 0, last = 0;
        // Split into vector of strings
        while ((cur = text.indexOf('\n', last)) != -1) {
            strings.addElement(text.substring(last, cur));
            last = cur + 1;
        }

        return strings;
    }

}
