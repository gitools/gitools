/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.tools.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.gitools.utils.tools.ToolDescriptor;
import org.gitools.utils.tools.ToolLifeCycle;
import org.gitools.utils.tools.args.BaseArguments;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolUsageException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.CmdLineParser;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @noinspection ALL
 */
public abstract class AbstractTool<Context> implements ToolLifeCycle<Context>
{

    private Context context;

    public Context getContext()
    {
        return context;
    }

    @Override
    public void initialize(Context context) throws ToolException
    {
        this.context = context;
    }

    @Override
    public void validate(Object argsObject) throws ToolException
    {
        if (!(argsObject instanceof BaseArguments))
        {
            return;
        }

        BaseArguments args = (BaseArguments) argsObject;
        if (args.loglevel != null)
        {
            Pattern pat = Pattern.compile("^(.*)=(.*)$");

            for (String loglevel : args.loglevel)
            {
                Matcher mat = pat.matcher(loglevel);
                if (!mat.matches() || mat.groupCount() != 2)
                {
                    throw new ToolValidationException("Invalid -loglevel argument: " + loglevel);
                }

                final String pkg = mat.group(1);
                final String levelName = mat.group(2);
                if (pkg == null || levelName == null)
                {
                    throw new ToolValidationException("Invalid -loglevel package: " + loglevel);
                }

                final Level level = Level.toLevel(levelName);
                if (level == null)
                {
                    throw new ToolValidationException("Invalid -loglevel level name: " + loglevel);
                }

                Logger.getLogger(pkg).setLevel(level);
            }
        }

        if (args.help)
        {
            throw new ToolUsageException();
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException
    {
        /*if (!(argsObject instanceof BaseArguments))
            return;
		
		BaseArguments args = (BaseArguments) argsObject;*/
    }

    @Override
    public void uninitialize() throws ToolException
    {
    }

    @Override
    public void printUsage(@NotNull PrintStream outputStream, String appName, @NotNull ToolDescriptor toolDesc, @NotNull CmdLineParser parser)
    {
        outputStream.print(toolDesc.getName() + " usage:\n\t" +
                appName + " " + toolDesc.getName());

        parser.printSingleLineUsage(outputStream);

        outputStream.println("\n");

        parser.printUsage(outputStream);

        outputStream.println();
    }
}
