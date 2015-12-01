package org.gitools.ui.app.actions.edit;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.PositionMapping;
import org.gitools.matrix.sort.AggregationFunction;
import org.gitools.utils.aggregation.MeanAggregator;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;


public class MeanFoldChange extends TransformFunction {
    private IMatrixLayer aggLayer;
    private final static Key<HashMatrix> CACHEKEY = new Key<HashMatrix>() {};
    private Heatmap heatmap;
    private IMatrixLayer layer;
    private IProgressMonitor monitor;
    private HashMatrix data;
    private static CountDownLatch latch;



    public MeanFoldChange(Heatmap heatmap, IMatrixLayer layer, IProgressMonitor monitor) {
        super("Mean Fold Change");
        this.heatmap = heatmap;
        this.layer = layer;
        this.monitor = monitor;
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {
        super.onBeforeIterate(parentIterable);

        AggregationFunction aggfunc = new AggregationFunction(layer, MeanAggregator.INSTANCE, heatmap.getDimension(MatrixDimensionKey.COLUMNS));

        IMatrixLayer aggLayer = new MatrixLayer("agg", Double.class);
        HashMatrix aggregationMatrix = new HashMatrix(
                new MatrixLayers(aggLayer),
                new HashMatrixDimension(ROWS, heatmap.getRows())
        );

        heatmap.newPosition()
                .iterate(heatmap.getDimension(MatrixDimensionKey.ROWS))
                .monitor(monitor.subtask(), "Preparing for '" + this.name + "' transformation")
                .transform(aggfunc)
                .store(
                        aggregationMatrix,
                        new PositionMapping().map(heatmap.getRows(), ROWS),
                        aggLayer
                );
        layer.setCache(CACHEKEY, aggregationMatrix);
    }

    private Double getMean(String identifier) {

        if (data == null) {
            data = (HashMatrix) layer.getCache(CACHEKEY);
            aggLayer = data.getLayers().get(0);
        }

        return (Double) data.get(aggLayer, identifier);
    }


    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if(value != null) {
            Double mean = getMean(position.get(MatrixDimensionKey.ROWS));
            //System.out.println(mean);
            if (mean == null) {
                return null;
            }
            String uuid = UUID.randomUUID().toString();
            System.out.println("\n" + position.get(MatrixDimensionKey.ROWS) + "/" + position.get(MatrixDimensionKey.COLUMNS) + ": " + mean +  ": " + value + ": " + uuid.substring(0,8));
            return value - mean;
        }
        return null;
    }
}
