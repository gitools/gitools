package es.imim.bg.ztools.ui.commands;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.stats.mtc.MultipleTestCorrection;
import es.imim.bg.ztools.ui.model.deprecated.ITableModel;

@Deprecated
public class MtcCommand implements Command {

	protected MultipleTestCorrection mtc;
	protected ITableModel tableModel;
	
	public MtcCommand(
			MultipleTestCorrection mtc, ITableModel tableModel) {
		
		this.mtc = mtc;
		this.tableModel = tableModel;
	}
	
	@Override
	public void execute(ProgressMonitor monitor) throws CommandException {
		
		final DoubleMatrix2D matrix = tableModel.getMatrix();
		
		for (int i = 0; i < matrix.columns(); i++) {
			DoubleMatrix1D values = matrix.viewColumn(i);
			mtc.correct(values);
		}
		
		tableModel.fireMatrixChanged();
	}
	
	/*protected ISectionModel sectionModel;
	
	public MtcBenjaminiHochbergFdrCommand(ISectionModel sectionModel) {
		this.sectionModel = sectionModel;
	}
	
	@Override
	public void execute(ProgressMonitor monitor) throws CommandException {
		MultipleTestCorrection mtc = new BenjaminiHochbergFdrMtc();
		
		final String paramName = "bhfdr-" + sectionModel.getTableName(
				sectionModel.getCurrentTable());
		
		final ITableModel tableModel = sectionModel.getTableModel();
		final DoubleMatrix2D matrix = tableModel.getVisibleMatrix();
		
		final DoubleMatrix2D correctedMatrix = matrix.copy();
		
		for (int i = 0; i < matrix.columns(); i++) {
			DoubleMatrix1D values = correctedMatrix.viewColumn(i);
			mtc.correct(values);
		}
		
		int newParam = sectionModel.addTable(paramName, correctedMatrix);
		sectionModel.setCurrentTable(newParam);
	}*/

}
