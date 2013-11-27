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
package org.gitools.ui.imageviewer;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.ui.heatmap.drawer.HeatmapDrawer;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class HeatmapCreateImageAction extends HeatmapAction {

    public HeatmapCreateImageAction() {
        super("Create Image");
        setLargeIcon(IconNames.CREATE_IMAGE_LARGE_ICON);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap heatmap = getHeatmap();

        final String title = JOptionPane.showInputDialog(Application.get(), "Write a title", getSelectedEditor().getName() + " (image)");

        // The user canceled the image createn
        if (title == null) {
            return;
        }


        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting heatmap to image ...", 1);

                    HeatmapDrawer drawer = new HeatmapDrawer(heatmap);
                    drawer.setPictureMode(true);

                    Dimension heatmapSize = drawer.getSize();

                    int type = BufferedImage.TYPE_INT_RGB;

                    final BufferedImage bi = new BufferedImage(heatmapSize.width, heatmapSize.height, type);
                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, heatmapSize.width, heatmapSize.height);
                    drawer.draw(g, new Rectangle(new Point(), heatmapSize), new Rectangle(new Point(), heatmapSize));

                    final BitmapImageViewer viewer = new BitmapImageViewer(bi);
                    viewer.setName(title);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Application.get().getEditorsPanel().addEditor(viewer);
                            Application.get().refresh();
                        }
                    });

                    monitor.end();
                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });

    }
}
