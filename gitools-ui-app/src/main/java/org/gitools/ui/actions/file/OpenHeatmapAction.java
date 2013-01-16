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

import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.MimeTypes;
import org.gitools.ui.IconNames;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.FileFormatFilter;

import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class OpenHeatmapAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenHeatmapAction() {
		super("Heatmap ...");
		setDesc("Open a heatmap from a file");
		setSmallIconFromResource(IconNames.openMatrix16);
		setLargeIconFromResource(IconNames.openMatrix24);
		setMnemonic(KeyEvent.VK_M);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileFilter[] filters = new FileFilter[] {
			// TODO new FileFormatFilter(FileFormats.HEATMAP),
			new FileFormatFilter("Known formats", null, new FileFormat[] {
				FileFormats.MULTIVALUE_DATA_MATRIX,
				FileFormats.DOUBLE_MATRIX,
				FileFormats.DOUBLE_BINARY_MATRIX,
                FileFormats.GENE_CLUSTER_TEXT,
				FileFormats.GENE_MATRIX,
				FileFormats.GENE_MATRIX_TRANSPOSED
			}),
			new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX),
			new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX.getTitle() + " (*.*)", MimeTypes.OBJECT_MATRIX),
			new FileFormatFilter(FileFormats.DOUBLE_MATRIX),
			new FileFormatFilter(FileFormats.DOUBLE_MATRIX.getTitle() + " (*.*)", MimeTypes.DOUBLE_MATRIX),
            new FileFormatFilter(FileFormats.GENE_CLUSTER_TEXT),
			new FileFormatFilter(FileFormats.DOUBLE_BINARY_MATRIX),
			new FileFormatFilter(FileFormats.GENE_MATRIX),
			new FileFormatFilter(FileFormats.GENE_MATRIX_TRANSPOSED)
		};

		final FileChooserUtils.FileAndFilter ret = FileChooserUtils.selectFile(
				"Select file", FileChooserUtils.MODE_OPEN, filters);

		if (ret == null)
			return;
		
		final File file = ret.getFile();
		
		if (file == null)
			return;

		final FileFormatFilter ff = (FileFormatFilter) ret.getFilter();

		Settings.getDefault().setLastPath(file.getParent());
		Settings.getDefault().save();

        JobRunnable loadFile = new CommandLoadFile(file.getAbsolutePath(), ff.getMime());
		JobThread.execute(AppFrame.instance(), loadFile);

		AppFrame.instance().setStatusText("Done.");
	}
}
