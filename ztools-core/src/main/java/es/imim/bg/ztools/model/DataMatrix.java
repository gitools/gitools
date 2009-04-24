package es.imim.bg.ztools.model;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import cern.colt.matrix.DoubleMatrix2D;

@XmlType(
		propOrder = {
				"name"/*, 
				"colNames", 
				"rowNames", 
				"data"*/})
				
public final class DataMatrix extends SimpleArtifact{

	protected String name;
	
	protected String[] colNames;
	protected String[] rowNames;
	
	protected DoubleMatrix2D data;
	
	public DataMatrix() {
	
	    //FIXME:
	    
	    super(null, null);
	    
	    String id [] = IdFactory.getUniqueIdentifier(this);
	    this.setId(id[0]);
	    this.setArtifactType(id[1]);
	    
	    	this.name = "";
		
	}

	public DataMatrix(
			String name, String[] colNames, 
			String[] rowNames, DoubleMatrix2D data) {
	   //FIXME:
	    
	    super(null, null);
	    
	    String id [] = IdFactory.getUniqueIdentifier(this);
	    this.setId(id[0]);
	    this.setArtifactType(id[1]);
	    
		this.name = name;
		this.colNames = colNames;
		this.rowNames = rowNames;
		this.data = data;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public final String[] getColNames() {
		return colNames;
	}

	public final void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	@XmlTransient
	public final String[] getRowNames() {
		return rowNames;
	}

	public final void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}

	@XmlTransient
	public final DoubleMatrix2D getData() {
		return data;
	}

	public final void setData(DoubleMatrix2D data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append('\n');
		sb.append(colNames).append('\n');
		sb.append(rowNames).append('\n');
		sb.append(data).append('\n');
		
		return sb.toString();
	}
}
