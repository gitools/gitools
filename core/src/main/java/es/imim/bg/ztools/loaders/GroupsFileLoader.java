package es.imim.bg.ztools.loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import es.imim.bg.csv.CSVException;
import es.imim.bg.csv.CSVProcessorAdapter;
import es.imim.bg.csv.CSVReader;
import es.imim.bg.ztools.model.Groups;

public class GroupsFileLoader implements GroupsLoader {

	private static final char defaultSeparator = '\t';
	private static final char defaultQuote = '"';
	
	private File file;
	private boolean growItems;
	
	private List<String> itemNames;
	
	private Map<String, Integer> itemIndexMap;
	
	public GroupsFileLoader(File file, boolean growItems) {
		this.file = file;	
		this.itemIndexMap = new HashMap<String, Integer>();
		this.growItems = growItems;
	}
	
	public GroupsFileLoader(File file) {
		this(file, true);
		
		itemNames = new ArrayList<String>();
	}
	
	public GroupsFileLoader(File file, List<String> itemNames) {
		this(file, false);
		
		this.itemNames = itemNames;
		
		// Create map between row names and its list index
		for (int i = 0; i < itemNames.size(); i++)
			itemIndexMap.put(itemNames.get(i), i);
	}
	
	public Groups load() throws IOException, CSVException {
		CSVReader csv = new CSVReader(file, defaultSeparator, defaultQuote);
		
		final List<String> groupNames = new ArrayList<String>();
		
		final Map<String, SortedSet<Integer>> groupItems = 
			new HashMap<String, SortedSet<Integer>>();
		
		csv.scan(new CSVProcessorAdapter() {
			
			String itemName;
			Integer itemIndex;
			
			@Override
			public boolean field(String field, int row, int col)
					throws CSVException {
				
				if (col == 0) {
					itemName = field;
					itemIndex = itemIndexMap.get(itemName);
					if (growItems && itemIndex == null) {
						itemIndex = itemNames.size();
						itemNames.add(itemName);
						itemIndexMap.put(itemName, itemIndex);
					}
				}
				else if (itemIndex != null){
					String groupName = field;
					SortedSet<Integer> itemIndices = groupItems.get(groupName);
					if (itemIndices == null) {
						itemIndices = new TreeSet<Integer>();
						groupNames.add(groupName);
						groupItems.put(groupName, itemIndices);
					}
					itemIndices.add(itemIndex);
					
				}

				return true;
			}
		});
		
		// Create indices array {group index}{items indices}
		
		int numGroups = groupNames.size();
		int[][] indices = new int[numGroups][];
		
		for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {
			String groupName = groupNames.get(groupIndex);
			SortedSet<Integer> itemIndices = groupItems.get(groupName);
			
			int numItems = itemIndices.size();
			int[] ii = indices[groupIndex] = new int[numItems];
			
			int k = 0;
			for (Integer itemIndex : itemIndices)
				ii[k++] = itemIndex;
		}
		
		return new Groups(itemNames, groupNames, indices);
	}
}
