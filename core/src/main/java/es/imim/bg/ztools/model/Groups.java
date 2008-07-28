package es.imim.bg.ztools.model;

import java.util.List;
public class Groups {

	private List<String> itemNames;
	
	private List<String> names;
	private int[][] indices;
	
	public Groups(List<String> itemNames, List<String> groupNames, int[][] indices) {
		this.itemNames = itemNames;
		this.names = groupNames;
		this.indices = indices;
	}

	public List<String> getNames() {
		return names;
	}
	
	public int[][] getIndices() {
		return indices;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int j = 0;
		for (int [] ii : indices) {
			sb.append(names.get(j++) + " : [");
			for (int k = 0; k < ii.length; k++) {
				if (k != 0)
					sb.append(", ");
				sb.append(itemNames.get(ii[k]));
			}
			sb.append("]\n");
		}
		
		return sb.toString();
	}
}
