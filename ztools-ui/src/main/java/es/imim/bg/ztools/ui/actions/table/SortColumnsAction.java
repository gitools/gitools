package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.aggregation.IAggregation;
import es.imim.bg.ztools.ui.aggregation.LogSumAggregation;
import es.imim.bg.ztools.ui.aggregation.MedianAggregation;
import es.imim.bg.ztools.ui.aggregation.MultAggregation;
import es.imim.bg.ztools.ui.aggregation.SumAggregation;
import es.imim.bg.ztools.ui.dialogs.SortColumnsDialog;
import es.imim.bg.ztools.ui.dialogs.SortDialog;
import es.imim.bg.ztools.ui.dialogs.SortDialog.AggregationType;
import es.imim.bg.ztools.ui.dialogs.SortDialog.SortCriteria;
import es.imim.bg.ztools.ui.dialogs.SortDialog.SortDirection;

public class SortColumnsAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	private ITable table;
	private ITableContents contents;
	private List<SortCriteria> criteriaList;

	public SortColumnsAction() {
		super("Sort columns by ...");	
		setDesc("Sort columns by ...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.table = getTable();
		if (table == null)
			return;
		
		this.contents = table.getContents();
		
		//select properties
		List<IElementProperty> cellProps = table.getCellAdapter().getProperties();
		ListIterator<IElementProperty> i = cellProps.listIterator();
		Object[] props = new Object[cellProps.size()];
		int counter = 0;
		while (i.hasNext()) {
			IElementProperty ep = i.next();
			props[counter] = ep.getName();
			counter++;
		}
				
		SortDialog d = new SortColumnsDialog(AppFrame.instance(), props);
		this.criteriaList = d.getValueList();
		boolean allCols = d.considerAllColumns();
		boolean allRows = d.considerAllRows();
		
		if(criteriaList != null) {
			//rows
			int rowCount;
			int[] rows = null;
			if(table.getSelectedRows().length == 0 || allRows) {
				rowCount = table.getVisibleRows().length;
				rows = new int[rowCount];
				for (int r = 0; r < rowCount; r++)
					rows[r] = r;
			} else
				rows = table.getSelectedRows();

			
			//cols
			Integer[] cols = null;
			final int colCount;
			if(allCols) {
				colCount = contents.getColumnCount();
				cols = new Integer[colCount];
				for (int j = 0; j < colCount; j++)
					cols[j] = j;
			}
			else {
				colCount = table.getColumnCount();
				cols = new Integer[colCount];
				int[] visibleCols = table.getVisibleColumns();
				for (int j = 0; j < colCount; j++)
					cols[j] = visibleCols[j];
			}

			Integer[] sortedColIndices = sortCols(rows, cols);
			int[] visibleSortedCols = new int[colCount];
			for (int k = 0; k < colCount; k++)
				visibleSortedCols[k] = sortedColIndices[k];
			table.setVisibleColumns(visibleSortedCols);
			AppFrame.instance()
				.setStatusText("Columns sorted.");
		}
	}

	private Integer[] sortCols(final int[] selectedRows,
								final Integer[] indices) {
		
		final List<IAggregation> aggregations = new ArrayList<IAggregation>();
		final List<Integer> properties = new ArrayList<Integer>();
		final List<Integer> directions = new ArrayList<Integer>();
		
		final int criterias = criteriaList.size();
		
		for (int i = 0; i < criteriaList.size(); i++) {
			SortCriteria sortCriteria = criteriaList.get(i);
			
			properties.add(sortCriteria.getPropertyIndex());
			
			SortDirection sd = sortCriteria.getDirection();
			Integer direction = (sd.equals(SortDirection.ASC)) ? 1 : -1;
			directions.add(direction);
						
			AggregationType at = sortCriteria.getAggregation();
			switch (at) {
			case MULTIPLICATION:
				aggregations.add(new MultAggregation());
				break;
			case LOGSUM:
				aggregations.add(new LogSumAggregation());
				break;
			case MEDIAN:
				aggregations.add(new MedianAggregation());
				break;
			case SUM:
				aggregations.add(new SumAggregation());
				break;
			}
		}
		
		final int N = selectedRows.length;
		DoubleFactory1D df = DoubleFactory1D.dense;
		final DoubleMatrix1D col1 = df.make(N);
		final DoubleMatrix1D col2 = df.make(N);

		
		Arrays.sort(indices, new Comparator<Integer>() {		
			@Override
			public int compare(Integer idx1, Integer idx2) {
				
				int level = 0;
				double aggr1 = 0, aggr2 = 0;

				
				while (aggr1 == aggr2 && level < criterias) {
						
					for (int i = 0; i < N; i++) {
						int row = selectedRows[i];
						
						Object value1 = contents.getCellValue(row , idx1, properties.get(level));
						double v1 = TableUtils.doubleValue(value1);
	
						if (!Double.isNaN(v1)) {
							col1.set(i, v1);
						}
						
						Object value2 = contents.getCellValue(row, idx2, properties.get(level));
						double v2 = TableUtils.doubleValue(value2);
	
						if (!Double.isNaN(v2)) {
							col2.set(i, v2);
						}
					}
					aggr1 = aggregations.get(level).aggregate(col1);
					aggr2 = aggregations.get(level).aggregate(col2);
					level++;
				}

				int res = (int) Math.signum(aggr1 - aggr2);
				level--;
				return res*directions.get(level);
			}
		});
		return indices;
	}
}
