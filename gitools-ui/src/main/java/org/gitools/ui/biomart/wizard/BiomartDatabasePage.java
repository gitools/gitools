package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.gitools.biomart.cxf.Mart;
import org.gitools.biomart.IBiomartService;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.FilteredListPage;

public class BiomartDatabasePage extends FilteredListPage {

	private static class DatabaseListWrapper {
		private Mart mart;
		
		public DatabaseListWrapper(Mart mart) {
			this.mart = mart;
		}
		
		public Mart getMart() {
			return mart;
		}
		
		@Override
		public String toString() {
			return mart.getDisplayName();
		}
	}

	private IBiomartService biomartService;
	
	private boolean updated;
	
	public BiomartDatabasePage(IBiomartService biomartService) {
		super();
		
		this.biomartService = biomartService;
		updated = false;
		
		setTitle("Select database");
	}

	@Override
	public void updateControls() {
		if (updated)
			return;
		
		setMessage(MessageStatus.PROGRESS, "Retrieving databases...");
		
		setListData(new Object[] {});
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {					
					List<Mart> registry = biomartService.getRegistry();
					final List<DatabaseListWrapper> listData = new ArrayList<DatabaseListWrapper>();
					for (Mart mart : registry)
						if (mart.getVisible() != 0)
							listData.add(new DatabaseListWrapper(mart));
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							setListData(listData.toArray(
									new DatabaseListWrapper[listData.size()]));
							
							setMessage(MessageStatus.INFO, "");
						}
					});
					
					updated = true;
				} catch (Exception e) {
					setStatus(MessageStatus.ERROR);
					setMessage(e.getMessage());
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), e);
					dlg.setVisible(true);
				}
			}
		}).start();
	}

	public Mart getMart() {
		DatabaseListWrapper model = (DatabaseListWrapper) getSelection();
		return model.getMart();
	}
}
