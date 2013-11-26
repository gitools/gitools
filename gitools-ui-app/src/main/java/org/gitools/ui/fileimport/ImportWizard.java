package org.gitools.ui.fileimport;

import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.utils.FileFormatFilter;

public interface ImportWizard extends IWizard, JobRunnable {

    FileFormatFilter getFileFormatFilter();

    void setLocator(IResourceLocator locator);
}
