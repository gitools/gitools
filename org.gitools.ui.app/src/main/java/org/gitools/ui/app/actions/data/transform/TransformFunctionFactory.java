package org.gitools.ui.app.actions.data.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.transform.FoldChangeFunction;
import org.gitools.matrix.transform.Log10Function;
import org.gitools.matrix.transform.LogNFunction;
import org.gitools.matrix.transform.SumConstantFunction;

import java.util.ArrayList;
import java.util.List;

public class TransformFunctionFactory {

    public static List<ConfigurableTransformFunction> get(Heatmap heatmap, MatrixLayer<Double> resultLayer) {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new LogNFunction());
        funcs.add(new Log10Function());
        funcs.add(new SumConstantFunction());
        funcs.add(new FoldChangeFunction(heatmap, resultLayer));
        return funcs;
    }
}
