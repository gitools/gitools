package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.MartLocation;

import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.wizard.common.FilteredListPanel;

public class BiomartDatabasePage extends AbstractWizardPage {


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

	private BiomartRestfulService biomartService;

	private FilteredListPanel panelDataset;
	
	private boolean updated;
	
	public BiomartDatabasePage(BiomartRestfulService biomartService /*IBiomartService biomartService*/) {
		super();
		
		this.biomartService = biomartService;
		updated = false;
		
		setTitle("Select database");


	}
	
	@Override
	public JComponent createControls() {
		panelDataset = new FilteredListPanel();

		panelDataset.list.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				selectionChangeActionPerformed();
			}
		});

		return panelDataset;
	}

	@Override
	public void updateControls() {
		if (updated)
			return;
		
		setMessage(MessageStatus.PROGRESS, "Retrieving databases...");
		
		panelDataset.setListData(new Object[] {});
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {					
					List<MartLocation> registry = biomartService.getRegistry();
					final List<DatabaseListWrapper> listData = new ArrayList<DatabaseListWrapper>();
					for (MartLocation mart : registry)
						if (mart.getVisible() != 0)
							listData.add(new DatabaseListWrapper(mart));
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							panelDataset.setListData(listData.toArray(
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

	public MartLocation getMart() {
		DatabaseListWrapper model = (DatabaseListWrapper) panelDataset.getSelectedValue();
		return model.getMart();
	}

	private void selectionChangeActionPerformed() {
		Object value = panelDataset.list.getSelectedValue();
		setComplete(value != null);
	}
}
