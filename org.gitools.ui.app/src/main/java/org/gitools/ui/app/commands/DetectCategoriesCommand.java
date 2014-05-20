package org.gitools.ui.app.commands;


import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.Heatmap;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;

public class DetectCategoriesCommand extends AbstractCommand {


    private Heatmap heatmap;
    private ArrayList<Double> categories;


    public DetectCategoriesCommand(Heatmap heatmap) {

        categories = new ArrayList<>();
        this.heatmap = heatmap;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws Command.CommandException {


        try {

            IMatrixPosition position = heatmap.newPosition();
            IMatrixIterable it = position.iterate(heatmap.getLayers().getTopLayer(), heatmap.getRows(), heatmap.getColumns())
                    .monitor(monitor, "Detecting categories")
                    .transform(new AbstractMatrixFunction<Double, Double>() {
                        @Override
                        public Double apply(Double v, IMatrixPosition position) {
                            if (v != null && !categories.contains(v)) {
                                categories.add((Double) v);
                                if (categories.size() > 30) {
                                    throw new CancellationException("Too many categories");
                                }
                            }
                            return null;
                        }
                    });
            ArrayList dummyList = newArrayList(it);

        } catch (CancellationException e) {
            categories.clear();
            throw new CancellationException(e.getMessage());
        }

        sort(categories);
    }

    public ArrayList<Double> getCategories() {
        return categories;
    }
}

