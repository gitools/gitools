package es.imim.bg.ztools.table.sort;

import es.imim.bg.ztools.aggregation.IAggregator;

public final class SortCriteria {

	public enum SortDirection {
		ASCENDING("Ascending", 1), 
		DESCENDING("Descending", -1);

		private String title;
		private int factor;

		private SortDirection(String title, int factor) {
			this.title = title;
			this.factor = factor;
		}

		public int getFactor() {
			return factor;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}

	protected Object property;
	protected int propertyIndex;
	protected IAggregator aggregator;
	protected SortDirection direction;

	public SortCriteria(
			Object prop, 
			int propIndex,
			IAggregator aggregator,
			SortDirection direction) {
		
		this.property = prop;
		this.propertyIndex = propIndex;
		this.direction = direction;
		this.aggregator = aggregator;
	}

	public final Object getProperty() {
		return property;
	}

	public final void setProperty(Object prop) {
		this.property = prop;
	}

	public final int getPropertyIndex() {
		return propertyIndex;
	}

	public final void setPropertyIndex(int propIndex) {
		this.propertyIndex = propIndex;
	}

	public final SortDirection getDirection() {
		return direction;
	}

	public final void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	public final IAggregator getAggregator() {
		return aggregator;
	}

	public final void setAggregator(IAggregator aggregator) {
		this.aggregator = aggregator;
	}

	@Override
	public String toString() {
		return property.toString() + ", "
			+ aggregator.toString() + ", "
			+ direction.toString();
	}
}
