package com.thedomokun.mobilogo;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.thedomokun.gui.MIDPFileBrowser;

public class MIDPMobiLogo extends MIDlet implements CommandListener {
    protected boolean firstTime = true;
    public MIDPCanvas logoComponent;
    public TextField source;
    public TextField input;
    public Form form;
    private SourceActionListener sourceActionListener;
    private SourceCommandListener sourceCommandListener;
    private EvalCommandListener evalCommandListener;
    private Command exitCommand, saveCommand, openCommand, runCommand, evalCommand;

    // font size
    public static int FONT_HEIGHT =
        Math.max(Font.getDefaultFont().getHeight(), 9);
    public static int EVAL_WIDTH =
        Font.getDefaultFont().stringWidth("Eval");

    public MIDPMobiLogo() {
        sourceActionListener = new SourceActionListener();
        sourceCommandListener = new SourceCommandListener();

        evalCommandListener = new EvalCommandListener();


        // TODO: Write up some unit tests to test the interpreter
        //String test = "FD 30 RT 90";
        //String test = "REPEAT 4 [ FD 30 RT 90 ]";
        String test = "TO SQUARE :SIZE REPEAT 4 [ FD :SIZE RT 90 ] END\n";
        test += "TO TRIANGLE :SIZE REPEAT 3 [ FD :SIZE RT 120 ] END\n";
        test += "TO HOUSE :SIZE SQUARE :SIZE FD :SIZE RT 30 TRIANGLE :SIZE LT 30 BK :SIZE END\n";
        test += "HOUSE 30\n";
        //String test = "MAKE \"X 10\nPRINT :X";
        //String test = "MAKE \"X SUM 10 2\nPRINT :X";
        //String test = "MAKE \"X SUM 10 2\nFD :X";

        logoComponent = new MIDPCanvas(Display.getDisplay(this), test);
    }

    protected void startApp() throws MIDletStateChangeException {
        if (firstTime) {
            Display display = Display.getDisplay(this);
            form = new Form(null);
            form.append(logoComponent);
            input = new TextField("Eval", "", 40, TextField.ANY);
            source = new TextField("Source", "", 500, TextField.ANY);

            form.setItemStateListener(sourceActionListener);

            source.setString(logoComponent.getSource());

            source.setItemCommandListener(sourceCommandListener);
            runCommand = new Command("Run", Command.ITEM, 1);
            source.addCommand(runCommand);

            input.setItemCommandListener(evalCommandListener);
            evalCommand = new Command("Eval", Command.ITEM, 1);
            input.addCommand(evalCommand);
            
            logoComponent.input = input;
            source.setLayout(Item.LAYOUT_VSHRINK);
            input.setLayout(Item.LAYOUT_VSHRINK);

            // Calculate source height based on font            
            input.setPreferredSize(form.getWidth() - EVAL_WIDTH - 20, FONT_HEIGHT + 2);
            source.setPreferredSize(form.getWidth(), FONT_HEIGHT * 4);
            logoComponent.setPreferredSize(form.getWidth(),
                                           (int)(form.getHeight() -
                                                   (FONT_HEIGHT * 5) - 30));
            logoComponent.setLayout(Item.LAYOUT_VEXPAND);
            
            form.append(input);
            form.append(source);
            
            // Add commands
            openCommand = new Command("Open", Command.ITEM, 1);
            saveCommand = new Command("Save", Command.ITEM, 2);
            exitCommand = new Command("Exit", Command.EXIT, 3);

            form.addCommand(openCommand);
            form.addCommand(saveCommand);
            form.addCommand(exitCommand);

            //loginForm.addCommand(openCommand);
            form.setCommandListener(this);

            display.setCurrent(form);

            logoComponent.start();

            firstTime = false;
        }
    }

    private class SourceActionListener implements ItemStateListener {        
        public void itemStateChanged(Item item) {
            if (item.equals(source)) {
                //System.out.println(item);
            }
        }
    }

    private class SourceCommandListener implements ItemCommandListener {        
        public void commandAction(Command c, Item item) {
            if (item.equals(source) && c.equals(runCommand)) {
                logoComponent.setSource(source.getString());
                logoComponent.canvas.interpret(true);
            }
        }
    }
    
    private class EvalCommandListener implements ItemCommandListener {        
        public void commandAction(Command c, Item item) {
            if (item.equals(input) && c.equals(evalCommand)) {
                StringBuffer buf = new StringBuffer();
                buf.append(source.getString());
                buf.append("\n");
                buf.append(input.getString());
                logoComponent.setSource(buf.toString());
                source.setString(buf.toString());
                logoComponent.canvas.interpret(true);
            }
        }
    }
    

    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        // TODO Auto-generated method stub
    }

    protected void pauseApp() {
        // TODO Auto-generated method stub
        
    }

    public void saveFileCallback(String fn) {
        // Save dialog returned with a file
        if (fn == null) {
            System.out.println("Null filename");
            return;
        }

        MobiLogoFile f = new MobiLogoFile();
        try {
            //Dialog.alert(logoComponent.source.getText());
            //System.out.println("Saving: " + source.getString());
            f.saveFile(fn, source.getString());
        } catch (IOException e) {
            System.out.println(e.toString());
            //Dialog.alert(e.toString());    
        }

        
    }
    
    public void openFileCallback(String fn) {
        // Open dialog returned with file
        if (fn == null) {
            System.out.println("Null filename");
            return;
        }
        MobiLogoFile f = new MobiLogoFile();
        try {
            String src = f.loadFile(fn);
            //System.out.println("file: " + fn + ", src: " + src);
            if (logoComponent != null) {
                logoComponent.setSource(src);
                source.setString(src);
                logoComponent.interpret(true);
            } else {
                //Dialog.alert("No canvas set");
            }
        } catch (IOException e) {
            //Dialog.alert(e.toString());
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        MIDPFileBrowser fb = new MIDPFileBrowser(Display.getDisplay(this), this);
        if (c == exitCommand) {
            try {
                destroyApp(false);
            } catch (MIDletStateChangeException e) {
                System.out.println(e);
            }
            notifyDestroyed();
        } else if (c == openCommand) {
            fb.setMode(MIDPFileBrowser.OPEN_MODE);
            try {
                fb.display();
            } catch (SecurityException e) {
                Alert alert =
                    new Alert("Error", "You are not authorized to access the restricted API", null,
                        AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);

                
                form.append(new StringItem(null,
                        "You cannot run this MIDlet with the current permissions. " +
                        "Sign the MIDlet suite, or run it in a different security domain"));
                Command exit = new Command("Exit", Command.EXIT, 3);
                form.addCommand(exit);
                form.setCommandListener(this);
                Display.getDisplay(this).setCurrent(alert, form);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (c == saveCommand) {
            fb.setMode(MIDPFileBrowser.SAVE_MODE);
            try {
                fb.display();
            } catch (SecurityException e) {
                Alert alert =
                    new Alert("Error", "You are not authorized to access the restricted API", null,
                        AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);

                form.append(new StringItem(null,
                        "You cannot run this MIDlet with the current permissions. " +
                        "Sign the MIDlet suite, or run it in a different security domain"));
                Command exit = new Command("Exit", Command.EXIT, 3);
                form.addCommand(exit);
                form.setCommandListener(this);
                Display.getDisplay(this).setCurrent(alert, form);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
