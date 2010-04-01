/*
 *  Copyright 2010 xrafael.
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

/*
 * BiomartTableFilteringPage.java
 *
 * Created on Feb 23, 2010, 4:51:10 PM
 */
package org.gitools.ui.biomart.wizard;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.wizard.common.FilteredListPanel;

public class BiomartSourcePage extends AbstractWizardPage {

	// Biomart model wrappers
	private static class BiomartSourceWrapper {

		private BiomartSource source;

		public BiomartSourceWrapper(BiomartSource bs) {
			this.source = bs;
		}

		public BiomartSource getBiomartSource() {
			return source;
		}

		public void setBiomartSource(BiomartSource source) {
			this.source = source;
		}

		@Override
		public String toString() {
			return source.getDescription();
		}
	}

	private static class DatabaseListWrapper {

		private MartLocation mart;

		public DatabaseListWrapper(MartLocation mart) {
			this.mart = mart;
		}

		public MartLocation getMart() {
			return mart;
		}

		@Override
		public String toString() {
			return mart.getDisplayName();
		}
	}

	private static class DatasetListWrapper {

		private DatasetInfo dataset;

		public DatasetListWrapper(DatasetInfo dataset) {
			this.dataset = dataset;
		}

		public DatasetInfo getDataset() {
			return dataset;
		}

		@Override
		public String toString() {
			return dataset.getDisplayName();
		}
	}


	private BiomartRestfulService service;

	private boolean updated;
	private FilteredListPanel datasetPanel;
	private MartLocation lastMartSelected;

	public BiomartSourcePage() {
		super();

		initComponents();

		portalCombo.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					updateDatabase();
			}
		});

		databaseList.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				updateDatasets();
			}
		});

		datasetPanel = new FilteredListPanel() {
			@Override protected void selectionChanged() {
				Object value = datasetPanel.getSelectedValue();
				setComplete(value != null);
			}
		};
		datasetPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		dataFilterPanel.setLayout(new BorderLayout());
		dataFilterPanel.add(datasetPanel, BorderLayout.CENTER);

		updated = false;

		setTitle("Select dataset");
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emptyValuesButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        portalCombo = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        databaseList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dataFilterPanel = new javax.swing.JPanel();

        setBorder(null);

        jLabel1.setText("Portal");

        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        databaseList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(databaseList);

        jLabel2.setText("Databases");

        jLabel3.setText("Datasets");

        javax.swing.GroupLayout dataFilterPanelLayout = new javax.swing.GroupLayout(dataFilterPanel);
        dataFilterPanel.setLayout(dataFilterPanelLayout);
        dataFilterPanelLayout.setHorizontalGroup(
            dataFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        dataFilterPanelLayout.setVerticalGroup(
            dataFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(portalCombo, 0, 543, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataFilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(portalCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataFilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataFilterPanel;
    private javax.swing.JList databaseList;
    private javax.swing.ButtonGroup emptyValuesButtonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox portalCombo;
    // End of variables declaration//GEN-END:variables

	@Override
	public JComponent createControls() {
		return this;
	}

	@Override
	public void updateControls() {

		if (updated)
			return;

		List<BiomartSource> sources = BiomartSourceManager.getDefault().getSources();
		DefaultComboBoxModel sourcesModel = new DefaultComboBoxModel();
		for (BiomartSource source : sources)
			sourcesModel.addElement(new BiomartSourceWrapper(source));
		portalCombo.setModel(sourcesModel);

		updateDatabase();

		//Clear dataset list
		datasetPanel.setListData(new Object[0]);
		datasetPanel.resetFilterText();
	}

	public void updateDatabase() {
		if (portalCombo.getSelectedItem() == null)
			return;

		setMessage(MessageStatus.PROGRESS, "Retrieving databases...");

		//Clear database list
		databaseList.setModel(new DefaultListModel());
		datasetPanel.setListData(new Object[0]);
		datasetPanel.resetFilterText();

		BiomartSource bs = getSource();
		if (bs == null)
			return;

		try {
			service = null;
			service = BiomartServiceFactory.createRestfulService(bs);
		} catch (BiomartServiceException ex) {
			ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
			dlg.setVisible(true);
			setStatus(MessageStatus.ERROR);
			setMessage(ex.getMessage());
			// TODO close the wizard or throw up the exception (RuntimeException)
		}

		if (service == null)
			return;

		new Thread(new Runnable() {
			@Override public void run() {
				try {
					List<MartLocation> registry = service.getRegistry();
					DefaultListModel model = new DefaultListModel();
					for (MartLocation mart : registry)
						if (mart.getVisible() != 0)
							model.addElement(new DatabaseListWrapper(mart));

					databaseList.setModel(model);
					updated = true;
					setMessage(MessageStatus.INFO, "");
				}
				catch (final Exception ex) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override public void run() {
							setStatus(MessageStatus.ERROR);
							setMessage(ex.getMessage());
						}
					});
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
					dlg.setVisible(true);
				}
			}
		}).start();
	}

	private void updateDatasets() {

		// Avoid to process null values or unaltered database selections 
		if (databaseList.getSelectedValue() == null ||
				(lastMartSelected != null &&
				lastMartSelected.getName().equals(getDataBase().getName()))
				)
			return;

		setMessage(MessageStatus.PROGRESS, "Retrieving datasets...");

		datasetPanel.setListData(new Object[]{});
		datasetPanel.resetFilterText();
				
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					lastMartSelected = getDataBase();
					
					List<DatasetInfo> datasets = service.getDatasets(lastMartSelected);

					List<Object> model = new ArrayList();

					for (int i = 0; i < datasets.size(); i++) {
						DatasetInfo ds = datasets.get(i);
						if (ds.getVisible() != 0)
							model.add(new DatasetListWrapper(ds));
					}

					datasetPanel.setListData(model.toArray());

					updated = true;
					setMessage(MessageStatus.INFO, "");
				}
				catch (final Exception ex) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override public void run() {
							setStatus(MessageStatus.ERROR);
							setMessage(ex.getMessage());
						}
					});
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
					dlg.setVisible(true);
				}
			}
		}).start();
	}

	// GETTERS
	public BiomartSource getSource() {
		return portalCombo.getSelectedItem() != null ?
			((BiomartSourceWrapper) portalCombo.getSelectedItem()).getBiomartSource()
			: null;
	}

	public MartLocation getDataBase() {
		return databaseList.getSelectedValue() != null ?
			((DatabaseListWrapper) databaseList.getSelectedValue()).getMart()
			: null;
	}

	public DatasetInfo getDataset() {
		return datasetPanel.getSelectedValue() != null ?
			((DatasetListWrapper) datasetPanel.getSelectedValue()).getDataset()
			: null;
	}

	public BiomartRestfulService getService() {
		return service;
	}
}
