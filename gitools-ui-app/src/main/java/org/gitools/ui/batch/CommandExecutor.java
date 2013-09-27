/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.batch;

import org.gitools.ui.batch.tools.*;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

    private final static Map<String, ITool> TOOLS = new HashMap<String, ITool>();

    static {
        addTool(new LoadTool());
        addTool(new VersionTool());
        addTool(new AddHeaderTextLabelsTool());
        addTool(new AddHeaderColoredLabelsTool());
    }

    private static void addTool(@NotNull ITool tool) {
        TOOLS.put(tool.getName().toLowerCase(), tool);
    }

    public boolean checkArguments(@NotNull String[] args, @NotNull PrintWriter out) {

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

    @NotNull
    public String printUsage() {
        return errorMsg();
    }

    public void execute(@NotNull String[] args, @NotNull PrintWriter out) {

        if (args.length == 0) {
            out.println(errorMsg());
            return;
        }

        String toolName = args[0];
        String toolArgs[] = new String[args.length - 1];
        System.arraycopy(args, 1, toolArgs, 0, toolArgs.length);

        ITool tool = TOOLS.get(toolName.toLowerCase());

        if (tool == null) {
            out.println(errorMsg());
            out.flush();
            return;
        }


        if (tool.run(toolArgs, out)) {
            out.println("OK");
            out.flush();
        }

    }

    @NotNull
    private static String errorMsg() {
        StringBuilder msg = new StringBuilder();
        msg.append("ERROR | Unknown command. Valid commands: ");
        for (String tool : TOOLS.keySet()) {
            msg.append(" ").append(tool);
        }
        return msg.toString();
    }

}
