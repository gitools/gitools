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
package org.gitools.ui.app.svg;

import org.apache.commons.io.FilenameUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.FileFormats;
import org.gitools.ui.app.heatmap.drawer.HeatmapDrawer;
import org.gitools.ui.app.svg.jfreesvg.SVGGraphics2D;
import org.gitools.ui.app.wizard.SaveFileWizard;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;

public class ExportHeatmapSVGAction extends HeatmapAction {

    public ExportHeatmapSVGAction() {
        super("Heatmap to SVG...");

        setDesc("Export the heatmap to SVG file");
        setMnemonic(KeyEvent.VK_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SaveFileWizard saveWiz = SaveFileWizard.createSimple(
                "Export heatmap to SVG ...",
                FilenameUtils.getName(getSelectedEditor().getName()),
                Settings.get().getLastExportPath(),
                new FileFormat[]{FileFormats.SVG}
        );

        final Heatmap hm = getHeatmap();
        final HeatmapDrawer drawer = new HeatmapDrawer(hm);
        final Dimension heatmapSize = drawer.getSize();
        drawer.setPictureMode(true);

        if ((hm.getColumns().size()*hm.getRows().size()) > Settings.get().getSvgBodyLimit()) {
            saveWiz.setMessage(MessageStatus.WARN, "The body of the heatmap will be drawn as a bitmap because there are more than " + Settings.get().getSvgBodyLimit() + " cells.");
        }

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
                    monitor.begin("Exporting heatmap to SVG ...", 1);
                    monitor.info("File: " + file.getName());

                    SVGGraphics2D g = new SVGGraphics2D(heatmapSize.width, heatmapSize.height);
                    drawer.draw(g, new Rectangle(new Point(), heatmapSize), new Rectangle(new Point(), heatmapSize));


                    FileWriter out = new FileWriter(file);
                    out.write(g.getSVGDocument());
                    out.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Application.get().setStatusText("Image created.");
    }


}
