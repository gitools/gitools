package es.imim.bg.ztools.cli.convert;

import cern.jet.stat.Probability;
import es.imim.bg.ztools.cli.AbstractCliTool;
import es.imim.bg.ztools.cli.InvalidArgumentException;
import es.imim.bg.ztools.cli.RequiredArgumentException;
import es.imim.bg.ztools.cli.CliToolException;

public class ModuleSetConvertCliTool extends AbstractCliTool {

	@Override
	public void validateArguments(Object argsObject)
			throws RequiredArgumentException, InvalidArgumentException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int run(Object argsObject) 
			throws CliToolException {
		// TODO Auto-generated method stub
		
		System.out.println("k"+"\t" +"n"+ "\t" +"p"+ "\t" +"lpv"+ "\t" +"rpv"+ "\t" +"rppv"+ "\t" +"tpv"+ "\tRl\tRr\tRt");
		int n = 39;
		double p = 0.026426896012509773;
		for (int k = 0; k < n; k++) {
			double lpv = Probability.binomial(k, n, p);
			double rpv = k > 0 ? Probability.binomialComplemented(k-1, n, p) : 1.0;
			double tpv = lpv + rpv > 1.0 ? 1.0 : lpv + rpv;
			double rppv = k > 0 ? Probability.poissonComplemented(k-1, n*p) : 1.0;
			double rppv2 = 1 - Probability.poisson(k, n*p);
			System.out.println(k + "\t" + n + "\t" + p + "\t" + lpv + "\t" + rpv + "\t" + rppv2 + "\t" + tpv + "\t0\t0\t0");
		}
		return 0;
	}

}
