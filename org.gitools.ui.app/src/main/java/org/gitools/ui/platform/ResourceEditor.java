/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.platform;


import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.format.HeatmapFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.resource.Resource;
import org.gitools.ui.app.wizard.SaveFileWizard;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;

public abstract class ResourceEditor<R extends Resource> extends AbstractEditor<R> {

    private R resource;

    public ResourceEditor(R resource) {
        this.resource = resource;

        IResourceLocator locator = resource.getLocator();
        if (locator != null) {
            if (locator.getURL().getProtocol().equals("file")) {
                try {
                    File file = new File(locator.getURL().toURI());
                    setFile(file);
                } catch (URISyntaxException e) {
                }
            }
        } else {
            setDirty(true);
        }

        setSaveAllowed(true);
        setSaveAsAllowed(true);

    }

    @Override
    public R getModel() {
        return resource;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {

        resource.setGitoolsVersion(Application.getGitoolsVersion());

        File file = getFile();
        if (file == null) {
            doSaveAs(monitor);
            return;
        }

        try {
            ApplicationContext.getPersistenceManager().store(resource.getLocator(), resource, monitor);
        } catch (PersistenceException ex) {
            monitor.exception(ex);
        }

        setDirty(false);
    }

    protected String getFileName() {
        return getModel().getTitle();
    }

    protected abstract FileFormat[] getFileFormats();

    @Override
    public void doSaveAs(IProgressMonitor monitor) {

        resource.setGitoolsVersion(Application.getGitoolsVersion());

        File file = getFile();
        if (file != null) {
            Settings.get().setLastPath(file.getParent());
        }

        SaveFileWizard wiz = SaveFileWizard.createSimple(
                "Save",
                getFileName(),
                Settings.get().getLastPath(),
                getFileFormats()
        );

        WizardDialog dlg = new WizardDialog(Application.get(), wiz);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        Settings.get().setLastPath(wiz.getFolder());
        file = wiz.getPathAsFile();
        setFile(file);

        try {

            IResourceLocator newLocator = new UrlResourceLocator(file);
            ApplicationContext.getPersistenceManager().store(newLocator, resource, monitor);
            resource.setLocator(newLocator);

        } catch (PersistenceException ex) {
            monitor.exception(ex);
        }

        setDirty(false);
        Settings.get().addRecentFile(file.getAbsolutePath());
        Settings.get().save();

    }

    @Override
    public boolean doClose() {
        if (isDirty()) {
            int res = JOptionPane.showOptionDialog(Application.get(), "File " + getName() + " is modified.\n" +
                    "Save changes ?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Cancel", "Discard", "Save"}, "Save");

            if (res == -1 || res == 0) {
                return false;
            } else if (res == 2) {
                SaveFileWizard wiz = SaveFileWizard.createSimple("Save", getName(), Settings.get().getLastPath(), new FileFormat[]{new FileFormat("Heatmap", HeatmapFormat.EXTENSION)});

                WizardDialog dlg = new WizardDialog(Application.get(), wiz);
                dlg.setVisible(true);
                if (dlg.isCancelled()) {
                    return false;
                }

                Settings.get().setLastPath(wiz.getFolder());

                setFile(wiz.getPathAsFile());

                JobThread.execute(Application.get(), new JobRunnable() {
                    @Override
                    public void run(IProgressMonitor monitor) {
                        doSave(monitor);
                    }
                });
            }
        }

        onClose();

        return true;
    }

    protected void onClose() {
    }


}
