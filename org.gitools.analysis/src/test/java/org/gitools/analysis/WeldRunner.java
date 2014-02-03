package org.gitools.analysis;

import org.gitools.api.ApplicationContext;
import org.gitools.api.persistence.IPersistenceManager;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldRunner extends BlockJUnit4ClassRunner {

    private final Class<?> klass;
    private final WeldContainer container;

    public WeldRunner(final Class<Object> klass) throws InitializationError {
        super(klass);
        this.klass = klass;

        this.container = new StartMain(new String[0]).go();

        ApplicationContext.setPersistenceManager(container.instance().select(IPersistenceManager.class).get());
        ApplicationContext.setProgressMonitor(new StreamProgressMonitor(System.out, true, true));
    }

    /*
    @Override
    protected Object createTest() throws Exception {
        //final Object test = container.instance().select(klass).get();
        return test;
    }  */
}