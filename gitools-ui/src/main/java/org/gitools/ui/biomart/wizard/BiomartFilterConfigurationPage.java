/*
 *  Copyright 2009 chris.
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
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.ui.biomart.filter.FilterConfigurationPanel;

import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class BiomartFilterConfigurationPage extends AbstractWizardPage {

	private MartLocation mart;
	private DatasetInfo dataset;

	private DatasetConfig dsConfiguration;
	private FilterConfigurationPanel panel;
	private final BiomartRestfulService biomartService;
	
	//private final IBiomartService biomartService;
	//private BiomartSoapService biomartService;

	public BiomartFilterConfigurationPage(BiomartRestfulService biomartService /*IBiomartService biomartService*/) {
		this.biomartService = biomartService;
	}

	@Override
	public JComponent createControls() {
		panel = new FilterConfigurationPanel();

		return panel;
	}

	@Override
	public void updateControls() {
		//panel.setAddBtnEnabled(false);
		panel.setDatasetConfiguration(null);

		setStatus(MessageStatus.PROGRESS);
		setMessage("Retrieving available filters ...");

		if (panel.getDatasetConfiguration() == null) {
			new Thread(new Runnable() {
				@Override public void run() {
					updateControlsThread();
				}
			}).start();
		}
	}

	private void updateControlsThread() {
		try {
			if (dsConfiguration == null) {
				dsConfiguration = biomartService.getConfiguration(dataset);
			}

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override public void run() {
					//panel.setAddBtnEnabled(true);
					panel.setDatasetConfiguration(dsConfiguration);                                        
					setStatus(MessageStatus.WARN);
					setComplete(true); //Just for test cases

					setMessage("Fill values for different filters ");
				}
			});
		} catch (Exception ex) {
			setStatus(MessageStatus.ERROR);
			setMessage(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/*private void setAttributePages(List<AttributePage> attrPages) {
		panel.setAttributePages(attrPages);
	}*/

	public void setSource(MartLocation mart, DatasetInfo dataset) {
		this.mart = mart;
		this.dataset = dataset;
	}

	public List<AttributeDescription> getFilterList() {
		return panel.getFilterList();
	}
}
