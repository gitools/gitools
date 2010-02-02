/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.dialog.progress;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import java.io.PrintStream;
import javax.swing.SwingUtilities;

public class ProgressJobMonitor extends StreamProgressMonitor {

	private final ProgressDialog dlg;

	public ProgressJobMonitor(ProgressDialog dlg, PrintStream out, boolean verbose, boolean debug) {
		super(out, verbose, debug);

		this.dlg = dlg;
	}

	private ProgressJobMonitor(IProgressMonitor parentMonitor, ProgressDialog dlg,
			PrintStream out, boolean verbose, boolean debug) {

		super(parentMonitor, out, verbose, debug);

		this.dlg = dlg;
	}

	@Override
	public void begin(final String title, final int totalWork) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (!dlg.isVisible())
                    dlg.setVisible(true);
				dlg.setMessage(title);
				dlg.setWork(totalWork);
				dlg.setProgress(0);
			}
		});

		super.begin(title, totalWork);
	}

	@Override
	public void title(final String title) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				dlg.setMessage(title);
			}
		});

		super.title(title);
	}

	@Override
	public void worked(int workInc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				dlg.setWork(totalWork);
				dlg.setProgress(getWorked());
			}
		});

		super.worked(workInc);
	}

	@Override
	public void end() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				dlg.setWork(totalWork);
				dlg.setProgress(getTotalWork());
			}
		});

		super.end();
	}

	@Override
	public void info(final String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				dlg.setInfo(msg);
			}
		});

		super.info(msg);
	}

	@Override
	protected void print(String text) {
		super.print(text);

		//TODO dlg.append(text);
	}

	@Override
	protected IProgressMonitor createSubtaskMonitor(
			IProgressMonitor parentMonitor,
			PrintStream out,
			boolean verbose,
			boolean debug) {

		return new ProgressJobMonitor(
				parentMonitor, dlg, out, verbose, debug);
	}
}
