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
package org.gitools.ui.app.actions.file;

import org.apache.commons.io.FilenameUtils;
import org.gitools.analysis.clustering.hierarchical.HierarchicalCluster;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.matrix.FileFormats;
import org.gitools.ui.app.actions.AbstractAction;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramPanel;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.wizard.common.SaveFileWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHierarchicalTreeImageAction extends AbstractAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportHierarchicalTreeImageAction() {
        super("Hierarchical tree to image...");

        setDesc("Export the hierarchical tree to image file");
        setMnemonic(KeyEvent.VK_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SaveFileWizard saveWiz = SaveFileWizard.createSimple(
                "Export hierarchical tree to image ...",
                FilenameUtils.getName(getSelectedEditor().getName()),
                Settings.get().getLastExportPath(),
                new FileFormat[]{FileFormats.PNG}
        );

        WizardDialog dlg = new WizardDialog(Application.get(), saveWiz);
        dlg.open();
        if (dlg.isCancelled()) {
            return;
        }

        Settings.get().setLastExportPath(saveWiz.getFolder());

        final File file = saveWiz.getPathAsFile();

        final String formatExtension = saveWiz.getFormat().getExtension();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting hierarchical tree to image ...", 1);
                    monitor.info("File: " + file.getName());

                    HierarchicalCluster model = getHierarchicalCluster();
                    final int width = getSelectedEditor().getWidth();
                    final int height = model.getIdentifiers().size()*12;

                    DendrogramPanel panel = new DendrogramPanel() {
                        @Override
                        public int getWidth() {
                            return width;
                        }

                        @Override
                        public int getHeight() {
                            return height;
                        }
                    };
                    panel.setModel(model);
                    panel.setBackground(Color.WHITE);

                    final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, width, height);
                    panel.paintTree(g);

                    ImageIO.write(bi, formatExtension, file);

                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().setStatusText("Image created.");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof HierarchicalCluster;
    }

    protected AbstractEditor getSelectedEditor() {
        return getEditorsPanel().getSelectedEditor();
    }

    protected EditorsPanel getEditorsPanel() {
        return Application.get().getEditorsPanel();
    }

    protected HierarchicalCluster getHierarchicalCluster() {

        IEditor editor = getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (!(model instanceof HierarchicalCluster)) {
            throw new UnsupportedOperationException("This action is only valid on a hierarchical tree viewer");
        }

        return (HierarchicalCluster) model;
    }


}
