package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.ztools.stats.mtc.MultipleTestCorrection;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.ITableModel;

public class MtcAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	protected MultipleTestCorrection mtc;
	
	public MtcAction(MultipleTestCorrection mtc) {
		super(mtc.getName());
		
		setDesc("Calculate " + mtc.getName() + " multiple test correction");
		
		this.mtc = mtc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText(mtc.getName() + " not implemented yet.");
		
		/*ISectionModel sectionModel = getSectionModel();
		ITableModel tableModel = sectionModel.getTableModel();
		
		if (tableModel == null)
			return;
		
		final DoubleMatrix2D matrix = tableModel.getMatrix();
		
		for (int i = 0; i < matrix.columns(); i++) {
			DoubleMatrix1D values = matrix.viewColumn(i);
			mtc.correct(values);
		}
		
		tableModel.fireMatrixChanged();
		
		AppFrame.instance()
			.setStatusText(mtc.getName() + " done.");*/
	}
}
