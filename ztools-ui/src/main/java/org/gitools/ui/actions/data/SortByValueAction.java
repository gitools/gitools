package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ListIterator;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.sort.SortDialogSimple;
import org.gitools.ui.platform.AppFrame;

import org.gitools.aggregation.AggregatorFactory;
import org.gitools.matrix.sort.SortCriteria;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementProperty;

public class SortByValueAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	private SortSubject sortSubject;
	
	public enum SortSubject {
		ROW, COLUMN, BOTH
	}

	public SortByValueAction(SortSubject sortSubject) {
		super(null);	
		this.sortSubject = sortSubject;
		switch (this.sortSubject) {
		case COLUMN:
			setName("Sort columns ...");
			setDesc("Sort columns ...");
			break;
		case ROW:
			setName("Sort rows ...");
			setDesc("Sort rows ...");
			break;
		case BOTH:
			setName("Sort rows and columns ...");
			setDesc("Sort rows and columns ...");
			break;
		}
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof MatrixFigure
			|| model instanceof IMatrixView;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		
		IMatrixView matrixView = getMatrixView();
		if (matrixView == null)
			return;
				
		//select properties
		List<IElementProperty> cellProps = matrixView.getCellAdapter().getProperties();
		ListIterator<IElementProperty> i = cellProps.listIterator();
		Object[] props = new Object[cellProps.size()];
		int counter = 0;
		while (i.hasNext()) {
			IElementProperty ep = i.next();
			props[counter] = ep.getName();
			counter++;
		}
		
		final SortDialogSimple d = new SortDialogSimple(
				AppFrame.instance(),
				getName(),
				true,
				props,
				AggregatorFactory.getAggregatorsArray());
		
		final List<SortCriteria> criteriaList = d.getCriteriaList();
		if (criteriaList.size() == 0)
			return;

		switch (this.sortSubject) {
			case COLUMN:
				new SortByValueActionColumnsDelegate(matrixView, criteriaList, false);
				AppFrame.instance().setStatusText("Columns sorted.");
				break;
			case ROW:
				new SortByValueActionRowsDelegate(matrixView, criteriaList, false);
				AppFrame.instance().setStatusText("Rows sorted.");
				break;
			case BOTH:
				new SortByValueActionRowsDelegate(matrixView, criteriaList, false);
				new SortByValueActionColumnsDelegate(matrixView, criteriaList, false);
				AppFrame.instance().setStatusText("Rows and columns sorted.");
				break;
		}
				

		
		
		
	}
}
