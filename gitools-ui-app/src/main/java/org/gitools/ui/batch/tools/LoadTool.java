package org.gitools.ui.batch.tools;

import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.platform.progress.JobRunnable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

public class LoadTool extends AbstractTool {

    @Argument(index = 0, metaVar = "<matrix-file>", required = true,
            usage = "Matrix file ")
    public String file;

    @Option(name = "-r", aliases = "--rows", metaVar = "<rows-annotation-file>",
            usage = "File rows annotations")
    public String rows;


    @Option(name = "-c", aliases = "--cols", metaVar = "<cols-annotation-file>",
            usage = "File cols annotations")
    public String cols;


    public LoadTool() {
        super();
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    protected JobRunnable newJob() {
        return new CommandLoadFile(file, rows, cols);
    }
}
