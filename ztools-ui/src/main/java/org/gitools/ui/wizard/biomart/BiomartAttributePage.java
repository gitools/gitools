package org.gitools.ui.wizard.biomart;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.AttributePage;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.wizard.biomart.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.wizard.common.FilteredTreePage;

public class BiomartAttributePage extends FilteredTreePage {
	
	private MartServiceSoap port;
	private Mart mart;
	private DatasetInfo dataset;
	
	private List<AttributePage> attrPages;
	
	private boolean updated;
	
	public BiomartAttributePage(MartServiceSoap port) {
		super();
	
		this.port = port;
		this.mart = null;
		this.dataset = null;
		this.attrPages = null;
		
		updated = false;
	}
	
	@Override
	public JComponent createControls() {
		JComponent component = super.createControls();
		panel.tree.setRootVisible(false);
		
		panel.tree.addTreeSelectionListener(new TreeSelectionListener() {	
			@Override public void valueChanged(TreeSelectionEvent e) {
				selectionChanged();
			}
		});
		
		return component;
	}
	
	@Override
	public void updateControls() {
		if (updated)
			return;
		
		setMessage("Retrieving available attributes ...");
		
		setComplete(false);
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					if (attrPages == null) {
						attrPages = port.getAttributes(
								dataset.getName(), mart.getServerVirtualSchema());
						
						setAttributePages(attrPages);
					}
					
					final AttributesTreeModel model = new AttributesTreeModel(attrPages);
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							setModel(model);
							expandAll();
							setMessage("");
						}
					});
					
					updated = true;
				}
				catch (Exception e) {
					setMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void selectionChanged() {
		final DefaultMutableTreeNode node = 
			(DefaultMutableTreeNode) panel.tree.getLastSelectedPathComponent();
		
		boolean complete = false;
		
		if (node != null && (node.getUserObject() instanceof AttributeWrapper)) {				
			AttributeWrapper aw = (AttributeWrapper) node.getUserObject();
			complete = aw.getType() == AttributeWrapper.NodeType.ATTRIBUTE;
		}
		
		setComplete(complete);
	}
	
	public void setSource(Mart mart, DatasetInfo dataset) {
		this.mart = mart;
		this.dataset = dataset;
		attrPages = null;
		updated = false;
	}

	public synchronized void setAttributePages(List<AttributePage> attrPages) {
		this.attrPages = attrPages;
	}
	
	public synchronized List<AttributePage> getAttributePages() {
		return attrPages;
	}

	@Override
	protected TreeModel createModel(String filterText) {
		return new AttributesTreeModel(attrPages, filterText);
	}
}
