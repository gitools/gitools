package es.imim.bg.ztools.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.ui.AppFrame;

public class SortDialog extends JDialog {
	
	private static final long serialVersionUID = -4832384734759927405L;
	
	static Object[] properties;
	static SortCriteria criteria;
	static List<SortCriteria> criteriaList; 
	static JFrame owner;
	static boolean switched = false;
	
	public SortDialog(JFrame owner) {
		super(owner);
		setModal(true);
		setLocationRelativeTo(owner);
		criteriaList = new ArrayList<SortCriteria>();
	}
	
	public List<SortCriteria> getCriteriaList() {
		setVisible(true);
		/*if advanced
			return new advanced(criteriaList).getCriteriaList();
		else*/
		return criteriaList;
	}

/*
	advanceClick() {
		advanced = true;
		setViseble(flase);
	}*/
	
	protected void discardChanges() {
		criteria = null;
		criteriaList = null;
		setVisible(false);
	}
	
	protected void newCriteria() {
		SortDialogSimple d = new SortDialogSimple(AppFrame.instance(), properties, "new Criteria");
		d.newCriteria();
	}	
}
