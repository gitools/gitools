/*
 *  Copyright 2010 chris.
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

import java.awt.event.ActionEvent;
import org.gitools.kegg.modules.EnsemblKeggModulesImporter;
import org.gitools.ui.IconNames;
import org.gitools.ui.modules.wizard.ModulesImportWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportGoModulesAction extends BaseAction {

	public ImportGoModulesAction() {
		super("Gene Ontology ...");
		setLargeIconFromResource(IconNames.GO24);
		setSmallIconFromResource(IconNames.GO16);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EnsemblKeggModulesImporter importer = new EnsemblKeggModulesImporter(false, true);

		ModulesImportWizard wz = new ModulesImportWizard(importer);
		wz.setTitle("Import Gene Ontology modules...");
		wz.setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_GO, 96));

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;


	}

}
