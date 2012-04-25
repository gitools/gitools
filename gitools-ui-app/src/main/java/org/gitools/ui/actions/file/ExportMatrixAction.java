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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import org.gitools.exporter.TextMatrixViewExporter;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportMatrixAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportMatrixAction() {
		super("Export matrix ...");
		
		setDesc("Export a matrix");
		setMnemonic(KeyEvent.VK_P);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = ActionUtils.getHeatmapMatrixView();
		if (matrixView == null)
			return;
		
		final List<IElementAttribute> properties = matrixView.getCellAdapter().getProperties();
		final String[] propNames = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			propNames[i] = properties.get(i).getName();

		int selectedPropIndex = matrixView.getSelectedPropertyIndex();
		selectedPropIndex = selectedPropIndex >= 0 ? selectedPropIndex : 0;
		selectedPropIndex = selectedPropIndex < properties.size() ? selectedPropIndex : 0;
		
		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do you want to export ?", "Export table data",
				JOptionPane.QUESTION_MESSAGE, null, propNames,
				propNames[selectedPropIndex]);

		if (selected == null || selected.isEmpty())
			return;

		int index = 0;
		for (int j = 0; j < propNames.length; j++)
			if (propNames[j].equals(selected))
				index = j;

		final int propIndex = index;

		final File file = FileChooserUtils.selectFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath(),
					FileChooserUtils.MODE_SAVE);

		if (file == null)
			return;

		Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting to image ...", 1);
					monitor.info("File: " + file.getName());
				
					TextMatrixViewExporter.exportMatrix(matrixView, propIndex, file);
		
					monitor.end();
				}
				catch (IOException ex) {
					monitor.exception(ex);
				}
			}
		});
		
		AppFrame.instance().setStatusText(selected + " exported.");
	}
}
