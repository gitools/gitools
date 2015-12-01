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
    private final boolean initiated;
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
        this.initiated = false;
        this.latch = new CountDownLatch(1);
        //System.out.println("\nMFC CREATED");
    }

    public void init() {
        String threadName = Thread.currentThread().getName();

        synchronized (this) {
            //System.out.println("I'll try \t" + threadName);
            if (latch.getCount() > 0) {
                System.out.println("INIT \t" + threadName);
                //Thread.currentThread().setPriority(Thread.MAX_PRIORITY-2);

                AggregationFunction aggfunc = new AggregationFunction(layer, MeanAggregator.INSTANCE, heatmap.getDimension(MatrixDimensionKey.COLUMNS));

                IMatrixLayer aggLayer = new MatrixLayer("agg", Double.class);
                HashMatrix aggregationMatrix = new HashMatrix(
                        new MatrixLayers(aggLayer),
                        new HashMatrixDimension(ROWS, heatmap.getRows())
                );

                heatmap.newPosition()
                        .iterate(heatmap.getDimension(MatrixDimensionKey.ROWS))
                        //.monitor(monitor.subtask(), "Prep")
                        .transform(aggfunc)
                        .store(
                                aggregationMatrix,
                                new PositionMapping().map(heatmap.getRows(), ROWS),
                                aggLayer
                        );
                layer.setCache(CACHEKEY, aggregationMatrix);
                latch.countDown();
                //Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                System.out.println("INIT DONE\t" + threadName);
            } else {
                //System.out.println("mehh: " + latch.getCount() + "\t" + threadName);
            }
        }
    }


    private Double getMean(String identifier) {

        if (data == null && layer.getCache(CACHEKEY) == null) {
            init();
        }

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
            System.out.println("\n" + position.get(MatrixDimensionKey.ROWS) + "/" + position.get(MatrixDimensionKey.COLUMNS) + ": " + mean + ": " + uuid.substring(0,8));
            return value - mean;
        }
        return null;
    }
}
