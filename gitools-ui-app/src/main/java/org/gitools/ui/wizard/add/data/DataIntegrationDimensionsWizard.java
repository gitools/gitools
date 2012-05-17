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

package org.gitools.ui.wizard.add.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.ui.platform.wizard.AbstractWizard;

public class DataIntegrationDimensionsWizard extends AbstractWizard {
    private static final long serialVersionUID = -6058042494975580570L;

    protected DataIntegrationPage dataIntegrationPage;
    protected DataDetailsPage dataDetailsPage;
    private Heatmap heatmap;

    public DataIntegrationDimensionsWizard(Heatmap heatmap) {
        super();
        this.heatmap = heatmap;
	setTitle("Add data to heatmap");
        setHelpContext("Integrate data dimensnsions.");
    }

	@Override
	public void addPages() {
        dataIntegrationPage = new DataIntegrationPage(heatmap);
        addPage(dataIntegrationPage);

        dataDetailsPage = new DataDetailsPage(heatmap);
        addPage(dataDetailsPage);
	}
	
	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performFinish() {
		// TODO Auto-generated method stub
		
	}
}
