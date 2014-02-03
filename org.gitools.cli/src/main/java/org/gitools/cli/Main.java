/*
 * #%L
 * gitools-cli
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
package org.gitools.cli;

import org.gitools.api.ApplicationContext;
import org.gitools.api.persistence.IPersistenceManager;
import org.gitools.persistence.PersistenceManager;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.tools.ToolManager;
import org.gitools.utils.tools.ToolSet;
import org.gitools.utils.tools.XmlToolSetResource;
import org.gitools.utils.tools.exception.ToolException;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.WeldContainer;
import org.kohsuke.args4j.CmdLineParser;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {

    private static final String appName = Main.class.getPackage().getImplementationTitle();

    private static final String versionString = Main.class.getPackage().getImplementationVersion();

    public static void main(String[] args) throws ToolException {

        // Initialize Weld and ApplicationContext
        WeldContainer container = new StartMain(args).go();
        ApplicationContext.setPersistenceManager(container.instance().select(IPersistenceManager.class).get());
        ApplicationContext.setProgressMonitor(new NullProgressMonitor());

        final ToolSet toolSet = XmlToolSetResource.load(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("gitools-cli.xml")));

        final ToolManager toolManager = new ToolManager(toolSet, appName, versionString);
        int code = toolManager.launch(args);
        System.exit(code);
    }

    public static void printVersion() {
        System.out.println(appName + " version " + versionString);
        System.exit(0);
    }

    private static void printUsage(PrintStream out, CmdLineParser parser, String toolName) {
        System.err.println("Usage: " + toolName + " [options]");
        parser.printUsage(System.err);
        System.err.println();
    }
}
