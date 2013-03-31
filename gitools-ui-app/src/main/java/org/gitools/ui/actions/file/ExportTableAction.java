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

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.gitools.exporter.TextMatrixViewExporter;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.dialog.attributes.AttributesSelectionDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportTableAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableAction() {
		super("Export table ...");
		
		setDesc("Export a table");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;

		final List<IElementAttribute> properties = matrixView.getCellAdapter().getProperties();
		final String[] attributeNames = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			attributeNames[i] = properties.get(i).getName();

		AttributesSelectionDialog dlg = new AttributesSelectionDialog(AppFrame.instance(), attributeNames);
		dlg.setVisible(true);

		if (dlg.getReturnStatus() != AttributesSelectionDialog.RET_OK) {
			AppFrame.instance().setStatusText("Table export cancelled.");
			return;
		}

		final File file = FileChooserUtils.selectFile(
						"Select destination file",
						Settings.getDefault().getLastExportPath(),
						FileChooserUtils.MODE_SAVE);

		if (file == null)
			return;

		Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

		final List<Integer> selectedIndices = dlg.getSelectedIndices();
		
		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting table ...", 1);
					monitor.info("File: " + file.getName());

					int[] attributeIndices = new int[selectedIndices.size()];
					for (int i = 0; i < selectedIndices.size(); i++)
						attributeIndices[i] = selectedIndices.get(i);

					TextMatrixViewExporter.exportTable(matrixView, attributeIndices, file);

					monitor.end();
				}
				catch (IOException ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Table exported.");
	}

}
