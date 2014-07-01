/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.heatmap.editor;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;
import com.google.common.base.Strings;
import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.format.HeatmapFormat;
import org.gitools.matrix.model.MatrixWrapper;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.app.commands.Command;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.heatmap.panel.ColorScalePanel;
import org.gitools.ui.app.heatmap.panel.HeatmapMouseListener;
import org.gitools.ui.app.heatmap.panel.HeatmapPanel;
import org.gitools.ui.app.heatmap.panel.details.DetailsPanel;
import org.gitools.ui.app.heatmap.panel.search.HeatmapSearchPanel;
import org.gitools.ui.app.wizard.SaveFileWizard;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.core.pages.common.SaveHeatmapFilePage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.MemoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;

public class HeatmapEditor extends AbstractEditor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeatmapEditor.class);
    private static final int DEFAULT_ACCORDION_WIDTH = 320;
    private static final int MINIMUM_AVAILABLE_MEMORY_THRESHOLD = (int) (3 * Runtime.getRuntime().maxMemory() / 10);

    private Heatmap heatmap;
    private HeatmapPanel heatmapPanel;
    private ColorScalePanel colorScalePanel;
    private HeatmapSearchPanel searchPanel;
    private DetailsPanel detailsPanel;
    private int lastMouseRow = -1;
    private int lastMouseCol = -1;
    private Timer timer;

    public HeatmapEditor(Heatmap heatmap) {

        IResourceLocator locator = heatmap.getLocator();
        if (locator != null && locator.getURL().getProtocol().equals("file")) {
            try {
                File file = new File(locator.getURL().toURI());
                setFile(file);
            } catch (URISyntaxException e) {
            }
        }


        // Initialize and create heatmap model
        heatmap.init();
        this.heatmap = heatmap;

        setIcon(IconUtils.getIconResource(IconNames.heatmap16));

        if (heatmap.getTitle() == null) {
            heatmap.setTitle(getName());
        }

        createComponents(this);

        setSaveAllowed(true);
        setSaveAsAllowed(true);
        setBackground(Color.WHITE);


        // Add change listeners
        PropertyChangeListener dirtyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                HeatmapEditor.this.setDirty(true);
            }
        };
        heatmap.addPropertyChangeListener(dirtyListener);
        heatmap.getRows().addPropertyChangeListener(dirtyListener);
        heatmap.getColumns().addPropertyChangeListener(dirtyListener);
        heatmap.getLayers().addPropertyChangeListener(dirtyListener);
        heatmap.getLayers().getTopLayer().getDecorator().addPropertyChangeListener(dirtyListener);

        // Create a timer that watches every 5 seconds the available memory
        // and detach the heatmap if it is below a minimum threshold.
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new CleanCacheTimer(), 5000, 5000);

    }

    @Override
    public void setName(String name) {
        if (this.heatmap != null && (this.heatmap.getTitle() == null || this.heatmap.getTitle().equals(""))) {
            heatmap.setTitle(name);
        }
        super.setName(name);
    }

    private void createComponents(JComponent container) {

        Dimension minimumSize = new Dimension(DEFAULT_ACCORDION_WIDTH, 100);

        detailsPanel = new DetailsPanel(heatmap);
        detailsPanel.setMinimumSize(minimumSize);

        colorScalePanel = new ColorScalePanel(heatmap);
        colorScalePanel.setMinimumSize(minimumSize);

        WebPanel emptyPanel = new WebPanel();
        emptyPanel.setBackground(Color.WHITE);
        GroupPanel leftPanel = new GroupPanel(GroupingType.fillMiddle, false, detailsPanel, emptyPanel, colorScalePanel);
        leftPanel.setUndecorated(true);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        leftPanel.setMinimumSize(minimumSize);

        heatmapPanel = new HeatmapPanel(heatmap);
        heatmapPanel.requestFocusInWindow();
        heatmapPanel.addHeatmapMouseListener(new HeatmapMouseListener() {
            @Override
            public void mouseMoved(int row, int col, MouseEvent e) {
                HeatmapEditor.this.mouseMoved(row, col, e);
            }

            @Override
            public void mouseClicked(int row, int col, MouseEvent e) {
                HeatmapEditor.this.mouseClicked(row, col, e);
            }
        });

        searchPanel = new HeatmapSearchPanel(heatmap, heatmapPanel);
        searchPanel.setVisible(false);

        JSplitPane splitPane = new WebSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, heatmapPanel);

        heatmapPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20), BorderFactory.createMatteBorder(0, 1, 1, 0, Color.GRAY)));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(DEFAULT_ACCORDION_WIDTH);
        splitPane.setLastDividerLocation(DEFAULT_ACCORDION_WIDTH);
        splitPane.setContinuousLayout(false);
        splitPane.setDividerSize(4);
        splitPane.setBackground(Color.WHITE);
        splitPane.setForeground(Color.WHITE);

        container.setLayout(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(splitPane, BorderLayout.CENTER);

    }

    @Override
    public Heatmap getModel() {
        return heatmap;
    }

    @Override
    public void refresh() {
    }

    @Override
    public void doVisible() {
        heatmapPanel.requestFocusInWindow();
    }

    @Override
    public void doSaveAs(IProgressMonitor monitor) {

        File file = getFile();
        if (file != null) {
            Settings.get().setLastPath(file.getParent());
        }

        heatmap.setGitoolsVersion(Application.getGitoolsVersion());

        String name = getName();
        int heatmapExt = name.indexOf(".heatmap");
        if (heatmapExt != -1) {
            name = name.substring(0, heatmapExt);
        }
        name = name.replaceAll("\\.", "_");

        SaveHeatmapFilePage page = new SaveHeatmapFilePage();
        page.setTitle("Save heatmap as");
        page.setFileNameWithoutExtension(name);
        page.setFolder(Settings.get().getLastPath());
        page.setFormats(new FileFormat[]{
                new FileFormat("Heatmap, single file (*.heatmap.zip)", HeatmapFormat.EXTENSION + ".zip", false, false),
                new FileFormat("Heatmap, multiple files (*.heatmap)", HeatmapFormat.EXTENSION, false, false)
        });
        if (heatmap.getRows().size() == heatmap.getContents().getRows().size() &&
                heatmap.getColumns().size() == heatmap.getContents().getColumns().size()) {
            page.enableDiscardHidden(false);
        } else {
            page.suggestDiscardHidden();
        }

        SaveFileWizard wiz = SaveFileWizard.createCustom(page);

        final WizardDialog dlg = new WizardDialog(Application.get(), wiz);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    dlg.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            throw new CancellationException();
        } catch (InvocationTargetException e) {
            throw new CancellationException();
        }

        if (dlg.isCancelled()) {
            return;
        }

        Settings.get().setLastPath(wiz.getFolder());
        file = wiz.getPathAsFile();
        setFile(file);

        IResourceLocator toLocator;
        if (heatmap.getLocator() == null) {
            toLocator = new UrlResourceLocator(file);
        } else {
            toLocator = new UrlResourceLocator(heatmap.getLocator().getReadFile(), file);
        }

        // Data matrix
        IMatrix data = heatmap.getData().get();

        if (page.isDiscardHidden()) {

            // Discard hidden data
            data = new MatrixWrapper(data) {
                @Override
                public IMatrixDimension getColumns() {
                    return heatmap.getColumns();
                }

                @Override
                public IMatrixDimension getRows() {
                    return heatmap.getRows();
                }

                @Override
                public boolean isChanged() {
                    return true;
                }
            };

            heatmap.setData(new ResourceReference<>("data", data));

        } else if (page.isOptimizeData()) {

            // Optimize mtabix index
            data = new MatrixWrapper(data) {
                @Override
                public boolean isChanged() {
                    return true;
                }
            };

            heatmap.setData(new ResourceReference<>("data", data));

        } else {

            IResourceLocator locator = heatmap.getData().getLocator();
            heatmap.setData(new ResourceReference<>("data", data));
            heatmap.getData().setLocator(locator);
            heatmap.getData().setChanged(data.isChanged());
            heatmap.getData().setBaseName(toLocator.getBaseName() + "-data");

        }

        HeatmapDimension rows = heatmap.getRows();
        HeatmapDimension columns = heatmap.getColumns();
        rows.setAnnotationsReference(new ResourceReference<>(rows.getId().toString().toLowerCase() + "-annotations", rows.getAnnotations()));
        columns.setAnnotationsReference(new ResourceReference<>(columns.getId().toString().toLowerCase() + "-annotations", columns.getAnnotations()));

        doSave(toLocator, monitor);

    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        doSave(heatmap.getLocator(), monitor);
    }

    private void doSave(IResourceLocator toLocator, IProgressMonitor monitor) {

        File file = getFile();
        if (file == null) {
            doSaveAs(monitor);
            return;
        }


        heatmap.setGitoolsVersion(Application.getGitoolsVersion());

        // Last saved and author info.
        heatmap.setLastSaved(new Date());
        Settings settings = Settings.get();
        if (Strings.isNullOrEmpty(heatmap.getAuthorName())) {
            heatmap.setAuthorName(settings.getAuthorName());
            if (!Strings.isNullOrEmpty(settings.getAuthorEmail())) {
                heatmap.setAuthorEmail(settings.getAuthorEmail());
            }
        }


        try {
            ApplicationContext.getPersistenceManager().store(toLocator, heatmap, monitor);
        } catch (PersistenceException ex) {
            monitor.exception(ex);
        }

        setDirty(false);
        Settings.get().addRecentFile(file.getAbsolutePath());
        Settings.get().save();

        // Force to reload the data after save
        if (!toLocator.equals(heatmap.getLocator()) || heatmap.getData().get().isChanged()) {

            monitor.title("Reloading the heatmap...");

            EditorsPanel editorPanel = Application.get().getEditorsPanel();
            editorPanel.removeEditor(this);

            CommandLoadFile loadFile = new CommandLoadFile(toLocator.getURL());
            try {
                loadFile.execute(monitor);
            } catch (Command.CommandException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public boolean doClose() {
        if (isDirty()) {
            int res = JOptionPane.showOptionDialog(Application.get(), "File " + getName() + " is modified.\n" +
                    "Save changes ?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Cancel", "Discard", "Save"}, "Save");

            if (res == -1 || res == 0) {
                return false;
            } else if (res == 2) {
                SaveFileWizard wiz = SaveFileWizard.createSimple("Save heatmap", getName(), Settings.get().getLastPath(), new FileFormat[]{new FileFormat("Heatmap", HeatmapFormat.EXTENSION)});

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

        // Force free memory
        timer.cancel();
        timer.purge();

        heatmap.detach();

        if (heatmap.getData().getLocator() != null) {
            heatmap.getData().unload();
        }

        heatmap = null;

        return true;
    }

    public void showSearch(boolean searchColumns) {
        if (searchPanel.isVisible() && searchPanel.searchRows() != searchColumns) {
            searchPanel.close();
        } else {
            searchPanel.searchOnColumns(searchColumns);
            searchPanel.setVisible(true);
        }
    }

    void mouseMoved(int row, int col, MouseEvent e) {

    }

    void mouseClicked(int row, int col, MouseEvent e) {
    }

    @Override
    public void detach() {
        this.heatmap.detach();
    }

    public Collection<Box> getBoxes() {
        return detailsPanel.getBoxes();
    }


    private class CleanCacheTimer extends TimerTask {

        private long lastDetach;

        private CleanCacheTimer() {
            this.lastDetach = System.currentTimeMillis();
        }

        @Override
        public void run() {
            if (MemoryUtils.getAvailableMemory() < MINIMUM_AVAILABLE_MEMORY_THRESHOLD && ((System.currentTimeMillis() - lastDetach)) > 2000) {

                LOGGER.warn("Memory too low, cleaning cache.");
                this.lastDetach = System.currentTimeMillis();

                detach();
            }
        }
    }
}
