package org.gitools.utils.progressmonitor;

public class ProgressMonitor {

    private static ThreadLocal<IProgressMonitor> progressMonitorThreadLocal = new ThreadLocal<IProgressMonitor>() {
        @Override
        protected IProgressMonitor initialValue() {
            return new NullProgressMonitor();
        }
    };

    public static IProgressMonitor get() {
        return progressMonitorThreadLocal.get();
    }

    public static void set(IProgressMonitor progressMonitor) {
        progressMonitorThreadLocal.set(progressMonitor);
    }

}
