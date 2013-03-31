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

package org.gitools.utils.progressmonitor;

public interface IProgressMonitor {
	
	public void begin(String title, int totalWork);

	public void title(String title);
	
	public void worked(int workInc);

	public void cancel();
	
	public boolean isCancelled();
	
	public void end();
	
	public int getLevel();
	
	public void setLevel(int level);

	//public int getWorked();
	
	public IProgressMonitor subtask();

	public void info(String msg);
	public void debug(String msg);

	public void exception(Throwable cause);
}
