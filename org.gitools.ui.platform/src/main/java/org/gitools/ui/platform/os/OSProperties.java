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

import java.awt.event.InputEvent;

public class OSProperties {

    private static OSProperties INSTANCE;

    static {
        // Load OS specific properties
        if (SystemInfo.isMac) {
            INSTANCE = new OSXProperties();
        } else {
            INSTANCE = new OSProperties();
        }
    }

    public static OSProperties get() {
        return INSTANCE;
    }

    String ctrlKey = "CTRL";
    String shiftKey = "⇧";
    String altKey = "ALT";
    String metaKey = "META";
    protected int ctrlMask = InputEvent.CTRL_MASK;
    protected int shiftMask = InputEvent.SHIFT_MASK;
    protected int metaMask = InputEvent.META_MASK;
    protected int altMask = InputEvent.ALT_MASK;


    public OSProperties() {

    }


    public String getShiftKey() {
        return shiftKey;
    }

    public String getAltKey() {
        return altKey;
    }

    public String getMetaKey() {
        return metaKey;
    }


    public String getCtrlKey() {
        return ctrlKey;
    }

    public int getCtrlMask() {
        return ctrlMask;
    }

    public int getShiftMask() {
        return shiftMask;
    }

    public int getAltMask() {
        return altMask;
    }

    public int getMetaMask() {
        return metaMask;
    }
}
