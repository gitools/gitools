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
        funcs.add(new FillEmptyFunction());
        funcs.add(new FoldChangeFunction(heatmap, resultLayer));
        funcs.add(new LogNFunction());
        funcs.add(new Log10Function());
        funcs.add(new ReplaceValueFunction());
        funcs.add(new SumConstantFunction());
        return funcs;
    }

    public static <T extends ConfigurableTransformFunction> T createFromTemplate(T template) {
        T newInstance = template.createNew();
        copyParameters(template, newInstance);
        return newInstance;
    }

    protected static <T extends ConfigurableTransformFunction> void copyParameters(T from, T to) {
        for (String key : from.getParameters().keySet()) {
            to.getParameter(key).setParameterValue(from.getParameter(key).getParameterValue());
        }
    }
}
