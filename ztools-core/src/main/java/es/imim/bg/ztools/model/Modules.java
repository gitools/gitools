package es.imim.bg.ztools.model;

public class Modules {

	protected String[] moduleNames;
	protected String[] itemNames;
	
	protected int[][] itemIndices;
	
	protected int[] itemsOrder;

	public Modules() {
	}
	
	public Modules(
			String[] moduleNames, String[] itemNames, int[][] itemIndices, int[] itemsOrder) {
		
		this.moduleNames = moduleNames;
		this.itemNames = itemNames;
		this.itemIndices = itemIndices;
		this.itemsOrder = itemsOrder;
	}

	public final String[] getModuleNames() {
		return moduleNames;
	}

	public final void setModuleNames(String[] moduleNames) {
		this.moduleNames = moduleNames;
	}

	public final String[] getItemNames() {
		return itemNames;
	}

	public final void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}

	public final int[][] getItemIndices() {
		return itemIndices;
	}

	public final void setItemIndices(int[][] itemIndices) {
		this.itemIndices = itemIndices;
	}
	
	public int[] getItemsOrder() {
		return itemsOrder;
	}
	
	public void setItemsOrder(int[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}
}
