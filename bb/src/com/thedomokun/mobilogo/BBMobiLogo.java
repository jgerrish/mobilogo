/**
 * 
 */
package com.thedomokun.mobilogo;

// TODO: comment these out unless we have an API key
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;

import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * @author josh
 *
 */
public class BBMobiLogo extends UiApplication {
    private LabelField titleField;
    private BBEvalField input;
    private BBSourceField source;
    //private boolean firstTime;    
    private BBMobiLogoCanvas logoComponent;
    private final int font_height = Font.getDefault().getHeight();

    public BBMobiLogo() {
        // Add file access permissions
        // The following requires the application to be signed
        
        ApplicationPermissions perm = new ApplicationPermissions();
        perm.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
        perm.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
        ApplicationPermissionsManager.getInstance().invokePermissionsRequest(perm);
        
        
        // Open up initial file
        MobiLogoFile file;
        String test;
        // TODO: Real reading of startup file
        try {
            file = new MobiLogoFile();
            //test = file.readFile();
            //test = "TO SQUARE :SIZE REPEAT 4 [ FD :SIZE RT 90 ] END\n";
            //test += "TO FLOWER REPEAT 36 [RIGHT 10 SQUARE 50] END\nFLOWER";
            //test = "to spiral :size :angle\nif :size > 100 [stop]\nforward :size\n";
            //test += "right :angle\nspiral :size + 2 :angle\nend";
            //test += "spiral 0 91";
            test = "TO ARC REPEAT 9 [FORWARD 5 RIGHT 10] END\n";
            test += "TO STAR REPEAT 4 [ARC LEFT 180] END\n";
            test += "TO DESIGN REPEAT 8 [STAR RIGHT 45] END\n";
            test += "DESIGN\n";
        } catch (ControlledAccessException e2) {
            final String str = "Error accessing SD card.  You should change the application permissions in Options.";
            UiApplication.getUiApplication().invokeLater(new Runnable(){
                public void run() {
                    Dialog.inform(str);
                }
                });
            test = "TO SQUARE REPEAT 4 [ FD 30 RT 90 ] END\nSQUARE";
        }
        logoComponent = new BBMobiLogoCanvas(test);

        // Create the main screen and add fields
        BBMobiLogoScreen screen =
            new BBMobiLogoScreen(MainScreen.NO_VERTICAL_SCROLL);
        screen.logoComponent = logoComponent;

        titleField = new LabelField("MobiLogo");

        screen.add(titleField);
        screen.add(logoComponent);
        
        input = new BBEvalField("Eval: ", "", logoComponent);
        logoComponent.input = input;
        
        source = new BBSourceField("Source: ", "", logoComponent);
        logoComponent.source = source;

        source.setText(logoComponent.getSource());

        screen.add(input);

        VerticalFieldManager vmanager;
        vmanager = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL
                | VerticalFieldManager.VERTICAL_SCROLLBAR) {
          protected void sublayout(int maxWidth, int maxHeight){
              int width = maxWidth;
              int height = font_height * 2 + 2;
          super.sublayout(width, height);
          super.setExtent(width,height);
          }
          };

        vmanager.add(source);
        screen.add(vmanager);

        pushScreen(screen);

        //firstTime = false;
    }

    public static void main(String[] args) {
        new BBMobiLogo().enterEventDispatcher();
    }
    
}
