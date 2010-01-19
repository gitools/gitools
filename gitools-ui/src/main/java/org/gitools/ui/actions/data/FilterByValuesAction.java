package org.gitools.ui.actions.data;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.gitools.ui.actions.BaseAction;
import org.gitools._DEPRECATED.matrix.filter.ValueFilterCondition;
import org.gitools.matrix.filter.ValueFilterCriteria;
import org.gitools.ui.platform.AppFrame;


import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.filter.MatrixViewValueFilter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.ValueFilterDialog;

public class FilterByValuesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterByValuesAction() {
		super("Filter by values...");	
		setDesc("Filter by values");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;

		List<IElementAttribute> attributes = matrixView.getContents().getCellAdapter().getProperties();

		int pvalueIndex = -1;
		String[] attrNames = new String[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			attrNames[i] = attributes.get(i).getName();
			if (pvalueIndex == -1 && attrNames[i].contains("p-value"))
				pvalueIndex = i;
		}
		if (pvalueIndex == -1)
			pvalueIndex = 0;

		ArrayList<ValueFilterCriteria> initialCriteria = new ArrayList<ValueFilterCriteria>(1);
		initialCriteria.add(new ValueFilterCriteria(attrNames[pvalueIndex], pvalueIndex, CutoffCmp.LT, 0.05));
		
		ValueFilterDialog dlg = new ValueFilterDialog(AppFrame.instance(), attrNames, CutoffCmp.comparators, initialCriteria);
		dlg.setVisible(true);

		if (dlg.getReturnStatus() == ValueFilterDialog.RET_OK) {
			if (dlg.isApplyToRowsChecked())
				MatrixViewValueFilter.filterRows(
						matrixView, matrixView.getSelectedColumns(),
						dlg.getCriteriaList(), dlg.isAllCriteriaChecked(),
						dlg.isAllElementsChecked(), dlg.isInvertCriteriaChecked());

			if (dlg.isApplyToColumnsChecked())
				MatrixViewValueFilter.filterColumns(
						matrixView, matrixView.getSelectedRows(),
						dlg.getCriteriaList(), dlg.isAllCriteriaChecked(),
						dlg.isAllElementsChecked(), dlg.isInvertCriteriaChecked());

			AppFrame.instance().setStatusText("Filter applied.");
		}
		else
			AppFrame.instance().setStatusText("Filter cancelled.");
	}

	private boolean evaluateRow(int[][] eval, boolean allCells, boolean sameCell, int criteriaNb) {
		int colNb = eval[0].length;
		int[] newEval = new int[colNb];

		for (int i = 0; i < colNb; i++){
			int trues = 0;
			for (int j = 0; j < criteriaNb; j++) {
				trues += eval[j][i];
			}
			
			if (sameCell && trues == criteriaNb)
				newEval[i] = 1;
			else if (sameCell && trues != criteriaNb)
				newEval[i] = 0;
			else if (!sameCell && trues > 0)
				newEval[i] = 1;
			else
				newEval[i] = 0;
		}
		
		int trueCells = 0;
		for (int i = 0; i < colNb; i++)
			trueCells += newEval[i];
		
		boolean addRow = false;
		if (allCells && trueCells == colNb)
			addRow = true;
		else if (!allCells && trueCells > 0)
			addRow = true;
		
		return addRow;
	}

	private int evaluateCriteria(ValueFilterCriteria vc, double value) {
		ValueFilterCondition vcond = vc.getCondition();
		double conditionValue = vc.getValue();
		boolean evaluation = false;
		switch (vcond) {
			case EQ:
				evaluation = (value == conditionValue);
				break;
			case GE:
				evaluation = (value >= conditionValue);
				break;
			case GT:
				evaluation = (value > conditionValue);
				break;
			case LE:
				evaluation = (value <= conditionValue);
				break;
			case LT:
				evaluation = (value < conditionValue);
				break;
			case NE:
				evaluation = (value != conditionValue);
				break;
		}
		return (evaluation) ? 1 : 0;
	}
}
