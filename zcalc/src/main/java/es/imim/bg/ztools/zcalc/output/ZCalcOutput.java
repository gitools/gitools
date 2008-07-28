package es.imim.bg.ztools.zcalc.output;

import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.ztools.zcalc.analysis.ZCalcAnalysis;

public interface ZCalcOutput {
	
	public void save(ZCalcAnalysis analysis) throws IOException, DataFormatException;

}
