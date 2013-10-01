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
import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.persistence.formats.FileFormat;
import org.gitools.core.persistence.formats.FileFormats;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.scale.ScaleExportWizard;
import org.gitools.ui.settings.Settings;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.drawer.ColorScaleDrawer;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @noinspection ALL
 */
public class ExportScaleImageAction extends BaseAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportScaleImageAction() {
        super("Export scale as an image ...");

        setDesc("Export the scale as an image file");
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

		/*SaveFileWizard saveWiz = SaveFileWizard.createSimple(
                "Export scale to image ...",
				PersistenceUtils.getFileName(editor.getName()) + "-scale",
				Settings.getDefault().getLastExportPath(),
				new FileFormat[] {
					FileFormats.PNG,
					FileFormats.JPG
				});*/

        Heatmap hm = (Heatmap) model;
        Decorator cd = hm.getLayers().getTopLayer().getDecorator();
        final IColorScale scale = cd != null ? cd.getScale() : null;

        if (scale == null) {
            return;
        }

        final ScaleExportWizard wz = new ScaleExportWizard();
        wz.setTitle("Export scale to image ...");
        wz.getSavePage().setFileNameWithoutExtension(FilenameUtils.getName(editor.getName()) + "-scale");
        wz.getSavePage().setFolder(Settings.getDefault().getLastExportPath());
        wz.getSavePage().setFormats(new FileFormat[]{FileFormats.PNG, FileFormats.JPG});
        wz.setScale(scale);

        WizardDialog dlg = new WizardDialog(AppFrame.get(), wz);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        Settings.getDefault().setLastExportPath(wz.getSavePage().getFolder());

        final File file = wz.getSavePage().getPathAsFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        final String formatExtension = wz.getSavePage().getFormat().getExtension();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting scale to image ...", 1);
                    monitor.info("File: " + file.getName());

                    ColorScaleDrawer drawer = new ColorScaleDrawer(scale);
                    if (wz.isPartialRange()) {
                        drawer.setZoomRangeMin(wz.getRangeMin());
                        drawer.setZoomRangeMax(wz.getRangeMax());
                    }
                    //drawer.setPictureMode(true);

                    Dimension size = drawer.getSize();
                    size.width = wz.getScaleSize();

                    BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);

                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, size.width, size.height);

                    drawer.draw(g, new Rectangle(new Point(), size), new Rectangle(new Point(), size));

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
