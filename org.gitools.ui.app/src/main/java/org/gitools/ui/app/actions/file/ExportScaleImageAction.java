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
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.matrix.FileFormats;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.scale.ScaleExportWizard;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.colorscale.ColorScaleDrawer;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.formatter.ITextFormatter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportScaleImageAction extends HeatmapAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportScaleImageAction() {
        super("Color scale to image");

        setDesc("Export the scale as an image file");
        setMnemonic(KeyEvent.VK_S);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Heatmap hm = getHeatmap();
        HeatmapLayer layer = hm.getLayers().getTopLayer();
        Decorator cd = layer.getDecorator();

        final ITextFormatter textFormatter = layer.getShortFormatter();
        final IColorScale scale = cd != null ? cd.getScale() : null;

        if (scale == null) {
            return;
        }

        final ScaleExportWizard wz = new ScaleExportWizard();
        wz.setTitle("Export scale to image ...");
        wz.getSavePage().setFileNameWithoutExtension(FilenameUtils.getName(getSelectedEditor().getName()) + "-scale");
        wz.getSavePage().setFolder(Settings.getDefault().getLastExportPath());
        wz.getSavePage().setFormats(new FileFormat[]{FileFormats.PNG, FileFormats.JPG});
        wz.setScale(scale);

        WizardDialog dlg = new WizardDialog(Application.get(), wz);
        dlg.open();
        if (dlg.isCancelled()) {
            return;
        }

        Settings.getDefault().setLastExportPath(wz.getSavePage().getFolder());

        final File file = wz.getSavePage().getPathAsFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        final String formatExtension = wz.getSavePage().getFormat().getExtension();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting scale to image ...", 1);
                    monitor.info("File: " + file.getName());

                    ColorScaleDrawer drawer = new ColorScaleDrawer(scale, textFormatter);

                    Dimension size = drawer.getSize();
                    size.width = wz.getScaleSize();

                    BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);

                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, size.width, size.height);

                    drawer.draw(g, new Rectangle(new Point(), size), new Rectangle(new Point(), size));

                    ImageIO.write(bi, formatExtension, file);

                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().setStatusText("Image created.");
    }
}
