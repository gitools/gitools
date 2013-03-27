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

import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceProjectRef;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WorkspaceNode extends AbstractNode {

	private static final long serialVersionUID = 5411843318201716836L;

	private Workspace workspace;
	
	public WorkspaceNode(Workspace workspace) {
		super();
		
		this.workspace = workspace;
		
		for (WorkspaceProjectRef projectRef : workspace.getProjectReferences())
			add(new WorkspaceProjectNode(projectRef));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void refresh() {
		final Map<WorkspaceProjectRef, WorkspaceProjectNode> nodeMap =	new HashMap<WorkspaceProjectRef, WorkspaceProjectNode>();
		
		Enumeration<WorkspaceProjectNode> e = children();
		while (e.hasMoreElements()) {
			WorkspaceProjectNode node = e.nextElement();
			nodeMap.put(node.getProjectRef(), node);
		}
		
		// Check that current project nodes exist in file system
		for (Entry<WorkspaceProjectRef, WorkspaceProjectNode> entry : nodeMap.entrySet()) {
			final WorkspaceProjectRef project = entry.getKey();
			final WorkspaceProjectNode node = entry.getValue();

			final File file = null;
            //TODO file = PersistenceManager.get().getEntityFile(project).getParentFile();
			
			if (file != null && !file.exists())
				remove(node);
		}
		
		// Check for new project nodes
		List<WorkspaceProjectRef> projectRefs = workspace.getProjectReferences();
		for (WorkspaceProjectRef projectRef : projectRefs) {
			if (!nodeMap.containsKey(projectRef)) {
				WorkspaceProjectNode node = new WorkspaceProjectNode(projectRef);
				nodeMap.put(projectRef, node);
				add(node);
			}
		}
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	@Override
	public String getLabel() {
		return "workspace";
	}
}
