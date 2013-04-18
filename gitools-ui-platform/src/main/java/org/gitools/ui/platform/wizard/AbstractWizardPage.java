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
package org.gitools.ui.platform.wizard;

import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.help.HelpContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public abstract class AbstractWizardPage extends JPanel implements IWizardPage {

    private static final long serialVersionUID = -4330234851091328953L;

    @Nullable
    private String id;

    private IWizard wizard;

    private boolean pageComplete;

    private String title = "";

    private Icon logo;

    private MessageStatus status = MessageStatus.INFO;

    private String message = "";

    private HelpContext helpContext;

    @NotNull
    private final List<IWizardPageUpdateListener> listeners = new ArrayList<IWizardPageUpdateListener>();

    protected AbstractWizardPage() {
        this(null);
    }

    protected AbstractWizardPage(@Nullable String id) {
        this.id = id != null ? id : this.getClass().getCanonicalName();
        this.pageComplete = false;
        //this.helpContext = new HelpContext(this.getClass());
    }

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public IWizard getWizard() {
        return wizard;
    }

    @Override
    public void setWizard(IWizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public boolean isComplete() {
        return pageComplete;
    }

    protected void setComplete(boolean complete) {
        this.pageComplete = complete;
        fireUpdated();
    }

    @Nullable
    @Override
    public JComponent createControls() {
        return this;
    }

    @Override
    public void updateControls() {
        // do nothing
    }

    @Override
    public void updateModel() {
        // do nothing
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        fireUpdated();
    }

    @Override
    public Icon getLogo() {
        return logo;
    }

    protected void setLogo(Icon logo) {
        this.logo = logo;
    }

    @Override
    public MessageStatus getStatus() {
        return this.status;
    }

    protected void setStatus(MessageStatus status) {
        this.status = status;
        fireUpdated();
    }

    @Override
    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
        fireUpdated();
    }

    @Override
    public HelpContext getHelpContext() {
        return helpContext;
    }

    @Override
    public void addPageUpdateListener(IWizardPageUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePageUpdateListener(IWizardPageUpdateListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setHelpContext(HelpContext helpContext) {
        this.helpContext = helpContext;
    }

    public void setMessage(MessageStatus status, String message) {
        this.status = status;
        this.message = message;
        fireUpdated();
    }

    protected void fireUpdated() {
        for (IWizardPageUpdateListener l : listeners)
            l.pageUpdated(this);
    }
}
