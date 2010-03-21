package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.gitools.biomart.BiomartCentralPortalService;

/*import org.gitools.biomart.cxf.DatasetInfo;
import org.gitools.biomart.cxf.Mart;
import org.gitools.biomart.IBiomartService;*/
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.FilteredListPage;

public class BiomartDatasetPage extends FilteredListPage {

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
	
	//private IBiomartService biomartService;
	private BiomartCentralPortalService biomartService;

	private Mart mart;

	private boolean updated;
	
	public BiomartDatasetPage(BiomartCentralPortalService biomartService /*IBiomartService biomartService*/) {
		super();
		
		this.biomartService = biomartService;
		
		this.mart = null;
		updated = false;
		
		setTitle("Select dataset");
	}
	
	@Override
	public void updateControls() {
	
		if (mart == null)
			return;
	
		if (updated)
			return;

		setStatus(MessageStatus.PROGRESS);
		setMessage("Retrieving datasets for " + mart.getDisplayName() + " ...");
		
		setListData(new Object[] {});
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					List<DatasetInfo> dataSets = biomartService.getDatasets(mart);
					final List<DatasetListWrapper> visibleDataSets = new ArrayList<DatasetListWrapper>();
					for (DatasetInfo ds : dataSets)
						if (ds.getVisible() != 0)
							visibleDataSets.add(new DatasetListWrapper(ds));
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							setListData(visibleDataSets.toArray(
									new DatasetListWrapper[visibleDataSets.size()]));
							
							setMessage(MessageStatus.INFO, "");
						}
					});
					
					updated = true;
				}
				catch (Exception e) {
					setStatus(MessageStatus.ERROR);
					setMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void setMart(Mart mart) {
		if (this.mart != mart)
			updated = false;
		
		this.mart = mart;
	}
	
	public DatasetInfo getDataset() {
		DatasetListWrapper wrapper = (DatasetListWrapper) getSelection();
		return wrapper.getDataset();
	}
}
