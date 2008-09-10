package es.imim.bg.ztools.test.results;

public abstract class AbstractResult implements Result {
	
	public AbstractResult() {
	}
	
	public final String[] getNames() {
		
		final int numParams = getNumParams();
		
		final String[] paramNames = new String[numParams];
		fillParamNames(paramNames);
		
		return paramNames;
	}
	
	public final Object[] getValues() {
		
		final int numParams = getNumParams();
		
		final Object[] paramValues = new Object[numParams];
		fillParamValues(paramValues);
		
		return paramValues;
	}
	
	/*public final Map<String, Object> toMap() {
		final String[] paramNames = getParamNames();
		final Object[] array = new Object[paramNames.length];
		toArray(array);
		
		final Map<String, Object> map = new HashMap<String, Object>();
		
		for (int i = 0; i < paramNames.length; i++)
			map.put(paramNames[i], array[i]);
		
		return map;
	}*/
	
	protected abstract int getNumParams();
	
	protected abstract int fillParamNames(String[] array);
	
	protected abstract int fillParamValues(Object[] array);
}
