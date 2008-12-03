package es.imim.bg.ztools.cli;

public interface CliTool {

	void validateArguments(Object argsObject) 
		throws RequiredArgumentException, InvalidArgumentException;
	
	int run(Object argsObject) 
			throws CliToolException;
}
