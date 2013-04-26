/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.platform.os;

import com.apple.eawt.Application;

import java.awt.*;

public class OSXProperties extends OSProperties {

    public OSXProperties(Image dockLogo) {
        super();
        Application osxApp = Application.getApplication();
        osxApp.setDockIconImage(dockLogo);

        metaKey = "⌘";
        altKey = "⌥";
    }


    @Override
    public int getCtrlMask() {
        //fix mapping of OSX keyboard
        return metaMask;
    }

    @Override
    public String getCtrlKey() {
        //fix mapping of OSX keyboard
        return metaKey;
    }

    @Override
    public String getMetaKey() {
        //fix mapping of OSX keyboard
        return ctrlKey;
    }

    @Override
    public int getMetaMask() {
        //fix mapping of OSX keyboard
        return ctrlMask;
    }


}
