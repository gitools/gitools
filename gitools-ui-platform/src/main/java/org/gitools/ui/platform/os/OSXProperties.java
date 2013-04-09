package org.gitools.ui.platform.os;

import com.apple.eawt.Application;

import java.awt.*;

public class OSXProperties implements OSProperties {

    public OSXProperties(Image dockLogo)   {

        Application osxApp = Application.getApplication();
        osxApp.setDockIconImage(dockLogo);
    }
}
