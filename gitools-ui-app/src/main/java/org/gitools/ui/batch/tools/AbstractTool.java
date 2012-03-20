package org.gitools.ui.batch.tools;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class AbstractTool implements ITool {

    @Override
    public boolean check(String[] args, PrintWriter out) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            out.print("USAGE | "+ getName() + "\n");
            parser.printUsage(out, null);
            return false;
        } catch (Exception e) {
            out.println("ERROR " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean run(String[] args, PrintWriter out) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            execute();
        } catch (CmdLineException e) {
            out.print("USAGE | "+ getName() + "\n");
            parser.printUsage(out, null);
            return false;
        } catch (Exception e) {
            out.println("ERROR | " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void execute() {
        JobRunnable job = newJob();

        if (job != null) {

            AppFrame mainFrame = AppFrame.instance();

            // Trick to force window to front, the setAlwaysOnTop works on a Mac,  toFront() does nothing.
            mainFrame.toFront();
            mainFrame.setAlwaysOnTop(true);
            mainFrame.setAlwaysOnTop(false);

            JobThread.execute(mainFrame, job);
        }
    }


    protected abstract JobRunnable newJob();
}
