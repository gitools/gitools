package org.gitools.model.analysis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.model.matrix.AnnotationMatrix;

@XmlRootElement(name = "enrichmentAnalysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnrichmentAnalysis extends Analysis {

	private static final long serialVersionUID = -7476200948081644842L;
	
	/** Filter to aply at the original matrix **/
	protected double cutoff;
	protected String comparator;
	
	// FIXME: to Christian:
	// We need annotations when loading the matrices,
	// its true that we made Figures of them but we dont 
	// have annotations to populate their decorators 
	
	@XmlTransient
	protected AnnotationMatrix annotations;
	
	public EnrichmentAnalysis (){
		
	}
	
	public double getCutoff() {
		return cutoff;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

}
