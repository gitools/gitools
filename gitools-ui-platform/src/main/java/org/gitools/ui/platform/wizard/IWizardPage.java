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

package org.gitools.ui.platform.wizard;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.help.HelpContext;

public interface IWizardPage {

	String getId();
	
	void setId(String id);

	IWizard getWizard();
	
	void setWizard(IWizard wizard);

	boolean isComplete();

	JComponent createControls();
	
	void updateControls();

	public void updateModel();

	String getTitle();

	Icon getLogo();

	MessageStatus getStatus();
	
	String getMessage();

	HelpContext getHelpContext();
	void setHelpContext(HelpContext context);

	void addPageUpdateListener(IWizardPageUpdateListener listener);

	void removePageUpdateListener(IWizardPageUpdateListener listener);
}