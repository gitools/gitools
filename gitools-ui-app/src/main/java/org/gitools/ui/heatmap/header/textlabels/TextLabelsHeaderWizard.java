/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.ui.heatmap.header.textlabels;

import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.platform.wizard.AbstractWizard;

public class TextLabelsHeaderWizard extends AbstractWizard {

	private HeatmapDim hdim;
	private HeatmapTextLabelsHeader header;

	private TextLabelsSourcePage sourcePage;
	private TextLabelsConfigPage configPage;

	public TextLabelsHeaderWizard(HeatmapDim hdim, HeatmapTextLabelsHeader header) {
		this.hdim = hdim;
		this.header = header;
	}

	@Override
	public void addPages() {
		sourcePage = new TextLabelsSourcePage(hdim, header);
		addPage(sourcePage);

		configPage = new TextLabelsConfigPage(hdim, header);
		addPage(configPage);
	}

}
