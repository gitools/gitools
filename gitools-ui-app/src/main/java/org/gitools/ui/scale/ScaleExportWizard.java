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

package org.gitools.ui.scale;

import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.wizard.common.SaveFilePage;

public class ScaleExportWizard extends AbstractWizard {

	protected SaveFilePage savePage;
	protected ScaleExportConfigPage configPage;

	public ScaleExportWizard() {
		savePage = new SaveFilePage();
		configPage = new ScaleExportConfigPage();
	}

	public SaveFilePage getSavePage() {
		return savePage;
	}

	public void setScale(IColorScale scale) {
        
        if (scale instanceof NumericColorScale) {
            NumericColorScale nScale = (NumericColorScale) scale;
            configPage.setRange(nScale.getMinValue(), nScale.getMaxValue());
        }
		
	}

	@Override
	public void addPages() {
		addPage(savePage);
		addPage(configPage);
	}

	public boolean isPartialRange() {
		return configPage.isPartialRange();
	}

	public double getRangeMin() {
		return configPage.getRangeMin();
	}

	public double getRangeMax() {
		return configPage.getRangeMax();
	}

	public int getScaleSize() {
		return configPage.getScaleSize();
	}
}
