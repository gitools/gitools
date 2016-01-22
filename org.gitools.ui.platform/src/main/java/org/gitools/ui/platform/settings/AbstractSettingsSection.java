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
