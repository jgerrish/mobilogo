package com.thedomokun.mobilogo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;


public class LWUITMobiLogo extends MIDlet {
    //public MobiLogoCanvas logoCanvas;
    protected boolean firstTime = true;

    public LWUITComponent logoComponent;
    public TextArea source;
    public TextField input;
    public Form form;
    private InputActionListener inputActionListener;
    private SourceActionListener sourceActionListener;
    
    public LWUITMobiLogo() {
        // Initialize LWUIT
        Display.init(this);

        inputActionListener = new InputActionListener();
        sourceActionListener = new SourceActionListener();

        String test = "TO SQUARE REPEAT 4 [ FD 30 RT 90 ] END\nSQUARE";
        //logoCanvas = new MobiLogoCanvas(test);
        logoComponent = new LWUITComponent(Display.getInstance(), test);
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // TODO Auto-generated method stub

    }

    protected void startApp() throws MIDletStateChangeException {
        if (firstTime) {
            form = new Form();
            form.setLayout(new BorderLayout());
            form.addComponent(BorderLayout.CENTER,
                              logoComponent);

            source = new TextArea(2, 20);
            source.setGrowByContent(false);
            input = new TextField(20);
            input.setEditable(true);
            input.addActionListener(inputActionListener);

            source.addActionListener(sourceActionListener);
            source.setText(logoComponent.getSource());
            
            logoComponent.input = input;

            Container editors = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            editors.addComponent(source);
            editors.addComponent(input);
            form.addComponent(BorderLayout.SOUTH, editors);
            form.show();

            logoComponent.start();

            firstTime = false;
        }
    }

    private class SourceActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent evt) {
            logoComponent.setSource(source.getText());
            logoComponent.canvas.interpret(true);
            //logoCanvas.setSource(source.getText());
            //logoCanvas.interpret();
        }
    }
    private class InputActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            //logoCanvas.source
            //logoCanvas.interpret();
        }
    }
    protected void pauseApp() {
        // TODO Auto-generated method stub
        
    }
}
