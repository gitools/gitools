package org.gitools.ui.wizardmess;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WizardDataModel {
	
	Map<String,Object> dataModel;
	
	public WizardDataModel() {
		dataModel = new HashMap<String, Object>();
	}
	
	public void setValue(String key, Object value){
		dataModel.put(key, value);
	}
	
	public Object getValue(Object key) {
		if(dataModel.containsKey(key))
			return dataModel.get(key);
		else
			return null;
	}
	
	public void removeValue(String key) {
		dataModel.remove(key);
	}
	
	public void clear(){
		dataModel.clear();
	}
	
	public Set<String> getKeys(){
		return dataModel.keySet();
	}

}
