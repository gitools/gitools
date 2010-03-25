package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.gitools.biomart.soap.model.AttributeInfo;
import org.gitools.biomart.soap.model.AttributePage;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.soap.model.Mart;
 import org.gitools.biomart.BiomartCentralPortalSoapService;

import org.gitools.ui.biomart.panel.AttributesTreeModel;
import org.gitools.ui.biomart.panel.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.FilteredTreePage;
import org.gitools.ui.wizard.common.FilteredTreePanel;

public class BiomartAttributePage extends FilteredTreePage {
	
	//private IBiomartService biomartService;
	private BiomartCentralPortalSoapService biomartService;

	private Mart mart;
	private DatasetInfo dataset;
	
	private List<AttributePage> attrPages;

	private AttributeInfo attribute;
	
	private boolean updated;
	
	public BiomartAttributePage(BiomartCentralPortalSoapService biomartService /*IBiomartService biomartService*/) {
		super();
	
		this.biomartService = biomartService;
		
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

		setStatus(MessageStatus.PROGRESS);
		setMessage("Retrieving available attributes ...");
		
		setComplete(false);

		panel.setModel(new AttributesTreeModel(new ArrayList<AttributePage>(0)));
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					if (attrPages == null) {
						attrPages = biomartService.getAttributes(mart, dataset);
						
						setAttributePages(attrPages);
					}
					
					final AttributesTreeModel model = new AttributesTreeModel(attrPages);
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							FilteredTreePanel p = getPanel();
							p.setModel(model);
							p.expandAll();
							setMessage(MessageStatus.INFO, "");
						}
					});
					
					updated = true;
				}
				catch (Exception e) {
					setStatus(MessageStatus.ERROR);
					setMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), e);
					dlg.setVisible(true);
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
			if (complete)
				attribute = (AttributeInfo) aw.getObject();
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
		updated = false;
	}
	
	public synchronized List<AttributePage> getAttributePages() {
		return attrPages;
	}

	public AttributeInfo getAttribute() {
		return attribute;
	}

	@Override
	protected TreeModel createModel(String filterText) {
		return new AttributesTreeModel(attrPages, filterText);
	}
}
