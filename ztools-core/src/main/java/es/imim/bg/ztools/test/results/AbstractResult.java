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
	
	public final double[] getValues() {
		
		final int numParams = getNumParams();
		
		final double[] paramValues = new double[numParams];
		fillParamValues(paramValues);
		
		return paramValues;
	}
	
	protected abstract int getNumParams();
	
	protected abstract int fillParamNames(String[] array);
	
	protected abstract int fillParamValues(double[] array);
}
