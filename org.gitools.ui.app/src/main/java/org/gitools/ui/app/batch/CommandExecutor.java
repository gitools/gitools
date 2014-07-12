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
package org.gitools.ui.app.batch;

import org.gitools.ui.core.commands.tools.ITool;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    boolean noErrors;

    public boolean checkArguments(String[] args, PrintWriter out) {

        if (args.length == 0) {
            out.println(errorMsg());
            out.flush();
            return false;
        }

        List<String> argsList = new ArrayList<>();
        for (String s : args) {
            if (s.equals("")) {
                continue;
            }
            argsList.add(s);
        }

        if (!argsList.get(argsList.size() - 1).equals(";")) {
            argsList.add(";");
        }

        boolean allOk = true;
        while (argsList.contains(";")) {

            int sepIndex = argsList.indexOf(";");
            List<String> sublist = argsList.subList(0, sepIndex);
            allOk = checkArguments(sublist, out);
            if (!allOk) {
                return false;
            }

            for (int i = 0; i < sepIndex && argsList.size() > 0; i++) {
                argsList.remove(0);
            }
        }
        return allOk;
    }

    public void execute(String[] args, PrintWriter out) {

        if (args.length == 0) {
            out.println(errorMsg());
            return;
        }

        List<String> argsList = new ArrayList<>();
        for (String s : args) {
            argsList.add(s);
        }

        if (!argsList.get(argsList.size() - 1).equals(";")) {
            argsList.add(";");
        }


        // run all comands
        noErrors = true;
        while (argsList.contains(";") && noErrors) {

            if (!noErrors) {
                break;
            }

            int sepIndex = argsList.indexOf(";");
            List<String> sublist = argsList.subList(0, sepIndex);
            execute(sublist, out);
            for (int i = 0; i < sepIndex && argsList.size() > 0; i++) {
                argsList.remove(0);
            }
        }

    }


    private boolean checkArguments(List<String> args, PrintWriter out) {

        String toolName = args.get(0);
        args.remove(toolName);
        String toolArgs[] = args.toArray(new String[args.size()]);
        ITool tool = ToolFactory.get(toolName);

        if (tool == null) {
            out.println(errorMsg());
            out.flush();
            return false;
        }

        return tool.check(toolArgs, out);
    }


    private void execute(List<String> args, PrintWriter out) {

        String toolName = args.get(0);
        args.remove(toolName);
        String toolArgs[] = args.toArray(new String[args.size()]);

        ITool tool = ToolFactory.get(toolName.toLowerCase());

        if (tool == null) {
            out.println(errorMsg());
            out.flush();
            return;
        }


        if (tool.run(toolArgs, out)) {
            out.println("OK");
            out.flush();
        } else {
            out.println(tool.getExitMessage());
            out.println("NOT OK");
            out.flush();
            noErrors = false;
        }
    }


    private static String errorMsg() {
        StringBuilder msg = new StringBuilder();
        msg.append("ERROR | Unknown command. Valid commands: ");
        for (String tool : ToolFactory.TOOLS.keySet()) {
            msg.append(" ").append(tool);
        }
        return msg.toString();
    }

}
