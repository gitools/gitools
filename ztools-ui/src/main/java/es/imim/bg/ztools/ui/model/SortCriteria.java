package es.imim.bg.ztools.ui.model;

public final class SortCriteria {

	private int columnIndex;
	private int paramIndex;
	private boolean ascending;
	
	public SortCriteria(int columnIndex, int paramIndex, boolean ascending) {
		this.columnIndex = columnIndex;
		this.paramIndex = paramIndex;
		this.ascending = ascending;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public final int getParamIndex() {
		return paramIndex;
	}
	
	public final void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}
	
	public boolean isAscending() {
		return ascending;
	}
	
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{col=").append(columnIndex)
			.append(", par=").append(paramIndex)
			.append(", asc=").append(ascending).append("}");
		return sb.toString();
	}
}
