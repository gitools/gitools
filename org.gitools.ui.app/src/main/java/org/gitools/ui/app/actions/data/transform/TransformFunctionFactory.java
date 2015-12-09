package org.gitools.ui.app.actions.data.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.transform.*;

import java.util.ArrayList;
import java.util.List;

public class TransformFunctionFactory {

    public static List<ConfigurableTransformFunction> get(Heatmap heatmap, MatrixLayer<Double> resultLayer) {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new LogNFunction());
        funcs.add(new Log10Function());
        funcs.add(new SumConstantFunction());
        funcs.add(new FoldChangeFunction(heatmap, resultLayer));
        funcs.add(new ReplaceValueFunction());
        return funcs;
    }
}
