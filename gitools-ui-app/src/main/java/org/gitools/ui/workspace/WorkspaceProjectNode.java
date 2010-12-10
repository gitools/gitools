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

package org.gitools.ui.workspace;

import java.io.File;

import org.gitools.persistence.FileSuffixes;
import org.gitools.workspace.WorkspaceProjectRef;

public class WorkspaceProjectNode extends AbstractNode {

	private static final long serialVersionUID = 8882086396274624151L;

	private WorkspaceProjectRef projectRef;
	
	public WorkspaceProjectNode(WorkspaceProjectRef project) {
		this.projectRef = project;
	}
	
	public WorkspaceProjectRef getProjectRef() {
		return projectRef;
	}
	
	@Override
	public void expand() {
		super.expand();
		
		File projectFile = new File(projectRef.getUrl());
		
		File[] files = projectFile.listFiles();
		for (File file : files) {
			if (!file.getName().matches(FileSuffixes.PROJECT)) {
				FileNode node = new FileNode(file);
				add(node);
			}
		}
	}
	
	@Override
	public String getLabel() {
		File projectFile = new File(projectRef.getUrl());
		
		return projectFile.getName();
	}
}
