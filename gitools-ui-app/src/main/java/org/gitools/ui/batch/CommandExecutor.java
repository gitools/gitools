package org.gitools.ui.batch;

import org.gitools.ui.batch.tools.ITool;
import org.gitools.ui.batch.tools.LoadTool;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

    private final static Map<String, ITool> TOOLS = new HashMap<String, ITool>();
    
    static {
        addTool(new LoadTool());
    }

    private static void addTool(ITool tool) {
        TOOLS.put(tool.getName(), tool);
    }
    
    public boolean checkArguments(String[] args, PrintWriter out) {
        
        if (args.length == 0) {
            out.println(errorMsg());
            out.flush();
            return false;
        }

        String toolName = args[0];
        String toolArgs[] = new String[args.length - 1];
        System.arraycopy(args, 1, toolArgs, 0, toolArgs.length);

        ITool tool = TOOLS.get(toolName);

        if (tool == null) {
            out.println(errorMsg());
            out.flush();
            return false;
        }
        
        return tool.check(toolArgs, out);
    }
    
    public String printUsage() {
        return errorMsg();
    }

    public void execute(String[] args, PrintWriter out) {
        
        if (args.length == 0) {
            out.println(errorMsg());
            return;
        }
        
        String toolName = args[0];
        String toolArgs[] = new String[args.length - 1];
        System.arraycopy(args, 1, toolArgs, 0, toolArgs.length);
        
        ITool tool = TOOLS.get(toolName);
        
        if (tool == null) {
            out.println(errorMsg());
            out.flush();
            return;
        }


        
        if (tool.run(toolArgs, out)) {
            out.println("OK");
            out.flush();
        };
        
    }
    
    private static String errorMsg() {
        StringBuilder msg = new StringBuilder();
        msg.append("ERROR | Unknown command. Valid commands: ");
        for (String tool : TOOLS.keySet()) {
            msg.append(" ").append(tool);
        }
        return msg.toString();
    }

}
