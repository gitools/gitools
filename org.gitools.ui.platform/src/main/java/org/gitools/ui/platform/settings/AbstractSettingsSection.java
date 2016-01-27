/*
 * #%L
 * org.gitools.ui.platform
 * %%
 * Copyright (C) 2013 - 2016 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.platform.settings;

import com.jgoodies.binding.beans.Model;

import javax.swing.*;


public abstract class AbstractSettingsSection extends Model implements ISettingsSection {
    public static final String PROPERTY_DIRTY = "PROPERTY_DIRTY";
    private boolean dirty;

    public AbstractSettingsSection() {
        this.dirty = false;
    }

    @Override
    public abstract  String getName();

    @Override
    public abstract JPanel getPanel();

    @Override
    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        firePropertyChange(PROPERTY_DIRTY, !dirty, dirty);
    }
}
