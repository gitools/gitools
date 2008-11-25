package es.imim.bg.ztools.cli;

public interface Tool {

	int run(Object argsObject) 
			throws RequiredArgumentException, ToolException;
}
