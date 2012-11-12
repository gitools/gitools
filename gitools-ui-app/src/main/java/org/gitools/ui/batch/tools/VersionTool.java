package org.gitools.ui.batch.tools;

import org.gitools.ui.Main;

import java.io.PrintWriter;

public class VersionTool implements ITool {

    public VersionTool() {
        super();
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public boolean run(String[] args, PrintWriter out) {
        out.println("Gitools " + Main.class.getPackage().getImplementationVersion());
        return true;
    }

    @Override
    public boolean check(String[] args, PrintWriter out) {
        return true;
    }

}
