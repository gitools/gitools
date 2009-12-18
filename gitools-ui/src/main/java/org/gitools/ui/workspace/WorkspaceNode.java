package org.gitools.ui.workspace;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gitools.persistence.PersistenceManager;
import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceProjectRef;

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
		final Map<WorkspaceProjectRef, WorkspaceProjectNode> nodeMap =
			new HashMap<WorkspaceProjectRef, WorkspaceProjectNode>();
		
		Enumeration<WorkspaceProjectNode> e = children();
		while (e.hasMoreElements()) {
			WorkspaceProjectNode node = e.nextElement();
			nodeMap.put(node.getProjectRef(), node);
		}
		
		// Check that current project nodes exist in file system
		for (Entry<WorkspaceProjectRef, WorkspaceProjectNode> entry : nodeMap.entrySet()) {
			final WorkspaceProjectRef project = entry.getKey();
			final WorkspaceProjectNode node = entry.getValue();
			
			final File file = PersistenceManager.getDefault()
										.getEntityFile(project).getParentFile();
			
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
