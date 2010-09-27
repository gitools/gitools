package org.gitools.analysis.htest.enrichment;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.gitools.analysis.htest.HtestAnalysis;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnrichmentAnalysis extends HtestAnalysis implements Serializable {

	private static final long serialVersionUID = -7476200948081644842L;
	
	/** Discard data rows without any module annotation */
	protected boolean discardNonMappedRows;
	
	public EnrichmentAnalysis() {
	}

	public boolean isDiscardNonMappedRows() {
		return discardNonMappedRows;
	}

	public void setDiscardNonMappedRows(boolean discardNonMappedRows) {
		this.discardNonMappedRows = discardNonMappedRows;
	}
}
