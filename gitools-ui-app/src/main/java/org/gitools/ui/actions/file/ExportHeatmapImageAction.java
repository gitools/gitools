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
package org.gitools.ui.actions.file;

import org.apache.commons.io.FilenameUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.drawer.HeatmapDrawer;
import org.gitools.core.persistence._DEPRECATED.FileFormat;
import org.gitools.core.persistence._DEPRECATED.FileFormats;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFileWizard;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @noinspection ALL
 */
public class ExportHeatmapImageAction extends BaseAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportHeatmapImageAction() {
        super("Export heatmap as an image ...");

        setDesc("Export the heatmap as an image file");
        setMnemonic(KeyEvent.VK_I);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        AbstractEditor editor = ActionUtils.getSelectedEditor();
        if (editor == null) {
            return;
        }

        final Object model = editor.getModel();
        if (!(model instanceof Heatmap)) {
            return;
        }

        SaveFileWizard saveWiz = SaveFileWizard.createSimple("Export heatmap to image ...", FilenameUtils.getName(editor.getName()), Settings.getDefault().getLastExportPath(), new FileFormat[]{FileFormats.PNG, FileFormats.JPG});

        WizardDialog dlg = new WizardDialog(AppFrame.get(), saveWiz);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        Settings.getDefault().setLastExportPath(saveWiz.getFolder());

        final File file = saveWiz.getPathAsFile();

        final String formatExtension = saveWiz.getFormat().getExtension();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting heatmap to image ...", 1);
                    monitor.info("File: " + file.getName());

                    Heatmap hm = (Heatmap) model;

                    HeatmapDrawer drawer = new HeatmapDrawer(hm);
                    drawer.setPictureMode(true);

                    Dimension heatmapSize = drawer.getSize();

					/*int type = formatExtension.equals(FileChooserUtils.png) ?
                        BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;*/

                    int type = BufferedImage.TYPE_INT_RGB;

                    BufferedImage bi = new BufferedImage(heatmapSize.width, heatmapSize.height, type);
                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, heatmapSize.width, heatmapSize.height);
                    drawer.draw(g, new Rectangle(new Point(), heatmapSize), new Rectangle(new Point(), heatmapSize));

                    ImageIO.write(bi, formatExtension, file);

                    monitor.end();
                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Image created.");
    }


}
