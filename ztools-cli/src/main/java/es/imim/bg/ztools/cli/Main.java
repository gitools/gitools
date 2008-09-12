package es.imim.bg.ztools.cli;

public class Main {

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.err.println("The tool you want to run must be specified.");
			System.exit(-1);
		}
		
		String[] delegatedArgs = new String[args.length - 1];
		System.arraycopy(args, 1, delegatedArgs, 0, args.length - 1);
		
		String toolName = args[0];
		
		if (toolName.equals("zcalc"))
			es.imim.bg.ztools.cli.zcalc.Main.main(delegatedArgs);
		else if (toolName.equals("oncoz"))
			es.imim.bg.ztools.cli.oncoz.Main.main(delegatedArgs);
		else {
			System.err.println("The tool you want to run doesn't exit: " + toolName);
			System.exit(-1);
		}
	}

}
