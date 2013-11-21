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
package org.gitools.ui.platform.actions;

import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.IEditor;

import javax.swing.*;
import java.net.URL;

public abstract class BaseAction extends AbstractAction {

    private static final long serialVersionUID = 8312774908067146251L;

    private static final String SELECTED_PROP = "selected";

    public static final BaseAction separator = new SeparatorAction();

    private boolean defaultEnabled;


    public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic, boolean checkMode, boolean selected, String actionGroup) {
        super(name, icon);

        this.defaultEnabled = false;

        if (desc != null) {
            putValue(SHORT_DESCRIPTION, desc);
        }

        if (mnemonic != null) {
            putValue(MNEMONIC_KEY, mnemonic);
        }
    }

    public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
        this(name, icon, desc, mnemonic, false, false, null);
    }

    public BaseAction(String name, ImageIcon icon, boolean checkMode, boolean checked, String actionGroup) {
        this(name, icon, null, null, checkMode, checked, actionGroup);
    }

    public BaseAction(String name, boolean checkMode, boolean checked, String actionGroup) {
        this(name, null, null, null, checkMode, checked, actionGroup);
    }

    public BaseAction(String name, ImageIcon icon, String desc) {
        this(name, icon, desc, null);
    }

    public BaseAction(String name, ImageIcon icon) {
        this(name, icon, null, null);
    }

    public BaseAction(String name) {
        this(name, null, null, null);
    }

    protected String getName() {
        return getValue(NAME).toString();
    }

    protected void setName(String name) {
        putValue(NAME, name);
    }

    protected void setDesc(String desc) {
        putValue(SHORT_DESCRIPTION, desc);
    }

    protected void setMnemonic(int vk) {
        putValue(MNEMONIC_KEY, vk);
    }


    ImageIcon getSmallIcon() {
        return (ImageIcon) getValue(SMALL_ICON);
    }

    void setSmallIcon(ImageIcon icon) {
        putValue(SMALL_ICON, icon);
    }

    protected void setSmallIconFromResource(String name) {
        setSmallIcon(getIconResource(name));
    }

    protected String getDesc() {
        Object desc = getValue(SHORT_DESCRIPTION);

        if (desc == null) {
            return getName();
        }

        return String.valueOf(desc);
    }


    protected ImageIcon getLargeIcon() {
        return (ImageIcon) getValue(LARGE_ICON_KEY);
    }

    protected void setLargeIcon(ImageIcon icon) {
        putValue(LARGE_ICON_KEY, icon);
    }

    protected void setLargeIconFromResource(String name) {
        setLargeIcon(getIconResource(name));
    }


    private ImageIcon getIconResource(String name) {
        URL url = getClass().getResource(name);
        if (url == null) {
            url = getClass().getResource(IconUtils.nullResourceImage);
        }

        return new ImageIcon(url);
    }

    protected void setDefaultEnabled(boolean defaultEnabled) {
        this.defaultEnabled = defaultEnabled;
        setEnabled(defaultEnabled);
    }

    public boolean updateEnabledByEditor(IEditor editor) {
        boolean en = isEnabledByEditor(editor);
        setEnabled(en);
        return en;
    }

    protected boolean isEnabledByEditor(IEditor editor) {
        if (editor != null) {
            Object model = editor.getModel();
            if (model != null) {
                return isEnabledByModel(model);
            }
        }

        return defaultEnabled;
    }

    protected boolean isEnabledByModel(Object model) {
        return defaultEnabled;
    }
}
