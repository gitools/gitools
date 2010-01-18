package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import java.util.List;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.sort.SortDialogSimple;
import org.gitools.ui.platform.AppFrame;

import org.gitools.aggregation.AggregatorFactory;
import org.gitools.matrix.sort.SortCriteria;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.matrix.sort.MatrixColumnSorter;
import org.gitools.matrix.sort.MatrixRowAndColumnSorter;
import org.gitools.matrix.sort.MatrixRowSorter;
import org.gitools.matrix.sort.MatrixSorter;
import org.gitools.ui.actions.ActionUtils;

public class SortByValueAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	
	private MatrixSorter sorter;
	private String typeName;
	
	public enum SortSubject {
		ROW, COLUMN, BOTH
	}

	public SortByValueAction(SortSubject type) {
		super(null);

		switch (type) {
		case COLUMN:
			setName("Sort columns ...");
			setDesc("Sort columns ...");
			typeName = "Columns";
			sorter = new MatrixColumnSorter();
			break;

		case ROW:
			setName("Sort rows ...");
			setDesc("Sort rows ...");
			typeName = "Rows";
			sorter = new MatrixRowSorter();
			break;

		case BOTH:
			setName("Sort rows and columns ...");
			setDesc("Sort rows and columns ...");
			typeName = "Rows and columns";
			sorter = new MatrixRowAndColumnSorter();
			break;
		}
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
				
		//select properties
		List<IElementAttribute> cellProps = matrixView.getCellAdapter().getProperties();
		String[] propNames = new String[cellProps.size()];
		for (int i = 0; i < cellProps.size(); i++)
			propNames[i] = cellProps.get(i).getName();

		final SortDialogSimple d = new SortDialogSimple(
				AppFrame.instance(),
				getName(),
				true,
				propNames,
				AggregatorFactory.getAggregatorsArray());
		
		final List<SortCriteria> criteriaList = d.getCriteriaList();
		if (criteriaList.size() == 0) {
			AppFrame.instance().setStatusText("No criteria specified.");
			return;
		}

		SortCriteria[] criteriaArray = new SortCriteria[criteriaList.size()];
		sorter.sort(matrixView, criteriaList.toArray(criteriaArray));
		AppFrame.instance().setStatusText(typeName + " sorted.");
	}
}
