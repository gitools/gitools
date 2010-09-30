package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.MartLocation;

import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.common.wizard.FilteredListPanel;

public class BiomartDatasetPage extends AbstractWizardPage {

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

	private final BiomartRestfulService biomartService;

	private MartLocation mart;

	private FilteredListPanel panelDataset;

	private boolean updated;

	public BiomartDatasetPage(BiomartRestfulService biomartService /*IBiomartService biomartService*/) {
		super();

		this.biomartService = biomartService;

		this.mart = null;
		updated = false;

		setTitle("Select dataset");
	}

	@Override
	public JComponent createControls() {
		panelDataset = new FilteredListPanel();

		panelDataset.list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectionChangeActionPerformed();
			}
		});

		return panelDataset;
	}

	@Override
	public void updateControls() {

		if (mart == null) {
			return;
		}

		if (updated) {
			return;
		}

		setStatus(MessageStatus.PROGRESS);
		setMessage("Retrieving datasets for " + mart.getDisplayName() + " ...");

		panelDataset.setListData(new Object[]{});

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<DatasetInfo> dataSets = biomartService.getDatasets(mart);
					final List<DatasetListWrapper> visibleDataSets = new ArrayList<DatasetListWrapper>();
					for (DatasetInfo ds : dataSets) {
						if (ds.getVisible() != 0) {
							visibleDataSets.add(new DatasetListWrapper(ds));
						}
					}

					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							panelDataset.setListData(visibleDataSets.toArray(
									new DatasetListWrapper[visibleDataSets.size()]));

							setMessage(MessageStatus.INFO, "");
						}
					});

					updated = true;
				} catch (Exception e) {
					setStatus(MessageStatus.ERROR);
					setMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setMart(MartLocation mart) {
		if (this.mart != mart) {
			updated = false;
		}

		this.mart = mart;
	}

	public DatasetInfo getDataset() {
		DatasetListWrapper wrapper = (DatasetListWrapper) panelDataset.getSelectedValue();
		return wrapper.getDataset();
	}

	private void selectionChangeActionPerformed() {
		Object value = panelDataset.list.getSelectedValue();
		setComplete(value != null);
	}
}
