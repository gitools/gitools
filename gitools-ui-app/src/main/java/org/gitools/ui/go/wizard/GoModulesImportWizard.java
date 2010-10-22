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

package org.gitools.ui.go.wizard;

import org.gitools.modules.importer.ModulesImporter;
import org.gitools.ui.IconNames;
import org.gitools.ui.modules.wizard.ModulesImportWizard;
import org.gitools.ui.platform.IconUtils;

public class GoModulesImportWizard extends ModulesImportWizard {

	public GoModulesImportWizard(ModulesImporter importer) {
		super(importer);

		setTitle("Import Gene Ontology modules...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_GO, 96));
	}
}
