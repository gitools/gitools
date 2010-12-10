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

package org.gitools.ui.analysis.wizard;

import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;


public class DataFilePage extends SelectFilePage {

	private static final FileFormat[] formats = new FileFormat[] {
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.MODULES_2C_MAP,
			FileFormats.MODULES_INDEXED_MAP
	};

	public DataFilePage() {
		this(formats);
	}

	public DataFilePage(FileFormat[] formats) {
		super(formats);

		setTitle("Select data source");
		
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_DATA, 96));
	}

	@Override
	protected String getLastPath() {
		return Settings.getDefault().getLastDataPath();
	}

	@Override
	protected void setLastPath(String path) {
		Settings.getDefault().setLastDataPath(path);
	}
}
