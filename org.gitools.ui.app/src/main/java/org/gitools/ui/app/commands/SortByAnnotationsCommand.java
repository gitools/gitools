package org.gitools.ui.app.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.core.Application;

public class SortByAnnotationsCommand extends HeaderCommand {

    public SortByAnnotationsCommand(String heatmap, String side, String pattern, String sort) {
        super(heatmap, side, sort, pattern);
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        super.execute(monitor);
        if (getExitStatus() > 0) {
            return;
        }

        applySort();

        Application.get().refresh();

        setExitStatus(0);
        return;
    }
}
