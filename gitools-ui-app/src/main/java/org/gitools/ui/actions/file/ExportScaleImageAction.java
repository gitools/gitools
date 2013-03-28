/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.file;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.drawer.ColorScaleDrawer;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.scale.ScaleExportWizard;
import org.gitools.ui.settings.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

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
		if (editor == null)
			return;

		final Object model = editor.getModel();
		if (!(model instanceof Heatmap))
			return;

		/*SaveFileWizard saveWiz = SaveFileWizard.createSimple(
				"Export scale to image ...",
				PersistenceUtils.getFileName(editor.getName()) + "-scale",
				Settings.getDefault().getLastExportPath(),
				new FileFormat[] {
					FileFormats.PNG,
					FileFormats.JPG
				});*/

		Heatmap hm = (Heatmap) model;
		ElementDecorator cd = hm.getActiveCellDecorator();
		final IColorScale scale = cd != null ? cd.getScale() : null;

		if (scale == null)
			return;

		final ScaleExportWizard wz = new ScaleExportWizard();
		wz.setTitle("Export scale to image ...");
		wz.getSavePage().setFileNameWithoutExtension(PersistenceUtils.getFileName(editor.getName()) + "-scale");
		wz.getSavePage().setFolder(Settings.getDefault().getLastExportPath());
		wz.getSavePage().setFormats(new FileFormat[] {
					FileFormats.PNG,
					FileFormats.JPG });
		wz.setScale(scale);

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wz);
		dlg.setVisible(true);
		if (dlg.isCancelled())
			return;

		Settings.getDefault().setLastExportPath(wz.getSavePage().getFolder());

		final File file = wz.getSavePage().getPathAsFile();
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		final String formatExtension = wz.getSavePage().getFormat().getExtension();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
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

					BufferedImage bi = new BufferedImage(
							size.width, size.height, BufferedImage.TYPE_INT_RGB);

					Graphics2D g = bi.createGraphics();
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, size.width, size.height);

					drawer.draw(g,
							new Rectangle(new Point(), size),
							new Rectangle(new Point(), size));

					ImageIO.write(bi, formatExtension, file);

					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Image created.");
	}
}
