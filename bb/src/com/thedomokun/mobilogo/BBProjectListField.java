/**
 * 
 */
package com.thedomokun.mobilogo;

import java.util.Vector;

import com.thedomokun.gui.BBFileBrowser;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

/**
 * @author Joshua Gerrish
 *
 */
public class BBProjectListField extends ObjectListField {
    private Vector images;
    private Vector authors;
    private Vector descriptions;
    public String selectedItem;
    private Screen myscreen;
    
    public BBProjectListField() {
        super();
        images = new Vector();
        descriptions = new Vector();
        authors = new Vector();
    }

    public void addImage(Bitmap img) {
        images.addElement(img);
    }

    public void addAuthor(String n) {
        authors.addElement(n);
    }

    public void addDescription(String d) {
        descriptions.addElement(d);
    }

    public void setScreen(Screen s) {
        myscreen = s;
    }
    
    // We are going to take care of drawing the item.
    public void drawListRow(ListField listField, Graphics graphics, 
            int index, int y, int width) {
        if ((images.size() <= index) || (this.getSize() <= index) ||
            (descriptions.size() <= index))
            return;

        Font font = graphics.getFont();
        Font title_font = font.derive(Font.BOLD, 12);
        Font author_font = font.derive(Font.PLAIN, 12);
        Font desc_font = font.derive(Font.PLAIN, 10);

        String title = (String)this.get(index);
        String author = (String)authors.elementAt(index);
        String desc = (String)descriptions.elementAt(index);
        Bitmap icon = (Bitmap)images.elementAt(index);

        //int textHeight = getRowHeight() / 3;
        if (null != icon) {
            int offsetY = (this.getRowHeight() - icon.getHeight()) / 2;
            graphics.drawBitmap(1, y + offsetY, icon.getWidth(),
                    icon.getHeight(), icon, 0, 0);
            graphics.setFont(title_font);
            graphics.drawText(title, icon.getWidth() + 2, y,
                    DrawStyle.ELLIPSIS, width);
            graphics.setFont(author_font);
            graphics.drawText(author, icon.getWidth() + 2, y + 13,
                    DrawStyle.ELLIPSIS, width);
            graphics.setFont(desc_font);
            graphics.drawText(desc, icon.getWidth() + 2, y + 26,
                    DrawStyle.ELLIPSIS, width);
        } else {
            graphics.drawText("- " + title, 0, y, DrawStyle.ELLIPSIS, width);
        }
    }

    protected boolean navigationClick(int status, int time) {
        int sel = getSelectedIndex();
        String currFile = (String)get(this, sel);

        selectedItem = currFile;
        UiApplication.getUiApplication().popScreen(myscreen);
        return true;
    }

}
