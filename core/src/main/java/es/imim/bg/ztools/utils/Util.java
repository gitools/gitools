package es.imim.bg.ztools.utils;

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

	public static String notNullString(String string) {
		return string == null ? "" : string;
	}
	
	/*
	elapsedDays = diff / (24 * 60 * 60 * 1000);
	diff -= elapsedDays * (24 * 60 * 60 * 1000);
	elapsedHours = diff / (60 * 60 * 1000);
	diff -= elapsedHours * (60 * 60 * 1000);
	elapsedMins = diff / (60 * 1000);
	diff -= elapsedMins * (60 * 1000);
	elapsedSecs = diff / 1000;*/
}
