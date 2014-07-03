
package org.gitools.ui.app.batch.tools;

import org.gitools.ui.app.commands.Command;
import org.gitools.ui.app.commands.SortByAnnotationsCommand;
import org.kohsuke.args4j.Option;

public class SortByAnnotationTool extends HeaderTool {

    @Option(name = "-s", aliases = "--sort", metaVar = "<sort>", required = true,
            usage = "Sort according to header. Specify either asc[ending] or desc[ending].")
    protected String sort;

    @Option(name = "-p", aliases = "--pattern", metaVar = "<pattern>", required = true,
            usage = "The pattern of annotations as e.g. ${annotation-id}")
    private String pattern;

    public SortByAnnotationTool() {
        super();
    }


    @Override
    public String getName() {
        return "sort-by-annotation";
    }


    @Override
    protected Command newJob() {
        return new SortByAnnotationsCommand(heatmap, side.name(), pattern, sort);
    }
}
