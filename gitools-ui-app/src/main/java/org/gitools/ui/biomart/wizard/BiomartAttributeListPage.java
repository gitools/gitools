/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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

package org.gitools.ui.biomart.wizard;

import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.MartLocation;

import org.gitools.ui.biomart.panel.BiomartAttributeListPanel;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class BiomartAttributeListPage extends AbstractWizardPage {

	private MartLocation mart;
	
	private DatasetInfo dataset;

	private List<AttributePage> attrPages;

	private BiomartAttributeListPanel panel;

	private BiomartService biomartService;

	private DatasetConfig biomartConfig;

	private Boolean reloadData; //determine whether update panel data
	
	public BiomartAttributeListPage() {
	}

	@Override
	public JComponent createControls() {
		panel = new BiomartAttributeListPanel();
		panel.addAttributeListChangeListener(new BiomartAttributeListPanel.AttributeListChangeListener() {
			@Override public void listChanged() {
				setComplete(panel.getAttributeListSize() > 0);
			}
		});

		return panel;
	}

	@Override
	public void updateControls() {
		//panel.setAddBtnEnabled(false);

		if (panel != null && reloadData)
			panel.removeAllListAttributes();

		panel.setAttributePages(null);

		if (panel.getAttributePages() == null) {
			new Thread(new Runnable() {
				@Override public void run() {

					updateControlsThread();
					reloadData = false; //To avoid reload data when back button
					
				}
			}).start();
		}

		
	}

	private void updateControlsThread() {
		try {
			if (attrPages == null || reloadData){
				setStatus(MessageStatus.PROGRESS);
				setMessage("Retrieving available attributes ...");
				biomartConfig = biomartService.getConfiguration(dataset);
				attrPages = biomartConfig.getAttributePages();
			}

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override public void run() {
					//panel.setAddBtnEnabled(true);
					panel.setAttributePages(attrPages);

					setStatus(MessageStatus.WARN);
					setMessage("Press [Add...] button to select attributes");
				}
			});
		}
		catch (final Throwable cause) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override public void run() {
					setStatus(MessageStatus.ERROR);
					setMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage());
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), cause);
					dlg.setVisible(true);
				}
			});
		}
	}

	/*private void setAttributePages(List<AttributePage> attrPages) {
		panel.setAttributePages(attrPages);
	}*/

	public void setSource(BiomartService biomartService, MartLocation mart, DatasetInfo ds) {

		if (this.dataset != null && this.dataset.getName().equals(ds.getName()))
			reloadData = false;
		else
			reloadData = true;

		this.biomartService = biomartService;
		this.mart = mart;
		this.dataset = ds;
	}

	public List<AttributeDescription> getAttributeList() {
		return panel.getAttributeList();
	}

	public DatasetConfig getBiomartConfig() {
		return biomartConfig;
	}
}