package org.gitools.utils;

import cern.jet.stat.Probability;

public class Util {

	/*double zscore = pvalue < 0.5 ? 10.0 : -10.0;
	try {
		if (pvalue > 0.0 && pvalue < 1.0)
			zscore = Probability.normalInverse(1.0 - pvalue);
	}
	catch (IllegalArgumentException e) {}*/
	
	public static double pvalue2zscore(double pvalue) {
		double zscore;
		try {
			zscore = 
				pvalue == 0.0 ? 10 :
				pvalue == 1.0 ? -10 :
				Probability.normalInverse(1.0 - pvalue);
		}
		catch (IllegalArgumentException e) {
			zscore = Double.NaN;
		}
		return zscore;
	}
	
	public static double pvalue2rightzscore(double pvalue) {
		double zscore;
		try {
			zscore = 
				pvalue == 0.0 ? 10 :
				pvalue == 1.0 ? 0 :
				Probability.normalInverse(1.0 - pvalue);
		}
		catch (IllegalArgumentException e) {
			zscore = pvalue < 0.5 ? 10 : 0;
		}
		return zscore;
	}

	public static String notNullString(String string) {
		return string == null ? "" : string;
	}

}
