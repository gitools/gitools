package org.gitools.matrix.sort;

import org.gitools.aggregation.IAggregator;

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

	protected String attributeName;
	protected int attributeIndex;
	protected IAggregator aggregator;
	protected SortDirection direction;

	public SortCriteria(
			int attributeIndex,
			IAggregator aggregator,
			SortDirection direction) {
		
		this(null, attributeIndex, aggregator, direction);
	}

	public SortCriteria(
			String attributeName,
			int attributeIndex,
			IAggregator aggregator,
			SortDirection direction) {

		this.attributeName = attributeName;
		this.attributeIndex = attributeIndex;
		this.direction = direction;
		this.aggregator = aggregator;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public final int getAttributeIndex() {
		return attributeIndex;
	}

	public final void setAttributeIndex(int propIndex) {
		this.attributeIndex = propIndex;
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
		return attributeName + ", "
			+ aggregator.toString() + ", "
			+ direction.toString();
	}
}
