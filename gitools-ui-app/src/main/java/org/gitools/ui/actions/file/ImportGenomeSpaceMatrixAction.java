/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.file;

import org.apache.commons.lang.StringUtils;
import org.gitools.ui.IconNames;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.genomespace.GSFileBrowser;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.json.JSONException;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ImportGenomeSpaceMatrixAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportGenomeSpaceMatrixAction() {
		super("Genomespace matrix ...");
		setLargeIconFromResource(IconNames.gs24);
		setSmallIconFromResource(IconNames.gs16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        GSFileBrowser fileBrowser = null;
        try {
            fileBrowser = new GSFileBrowser(AppFrame.instance(), GSFileBrowser.Mode.OPEN);
            fileBrowser.setVisible(true);

            String fileURL = fileBrowser.getFileURL();

            if (!StringUtils.isEmpty(fileURL)) {
                JobRunnable loadFile = new CommandLoadFile(fileURL);
                JobThread.execute(AppFrame.instance(), loadFile);
                AppFrame.instance().setStatusText("Done.");
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

}
