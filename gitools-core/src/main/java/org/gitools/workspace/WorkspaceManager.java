/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.workspace;

import org.gitools.model.Project;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class WorkspaceManager
{

    private static final String WORKSPACE_FILE_NAME = "workspace.xml";

    private static WorkspaceManager instance;

    public static final WorkspaceManager getDefault()
    {
        if (instance == null)
        {
            instance = new WorkspaceManager();
        }
        return instance;
    }

    public static final void setDefault(WorkspaceManager ws)
    {
        instance = ws;
    }

    public static final WorkspaceManager createManager(File xmlPath)
    {
        File basePath = xmlPath.getParentFile();
        if (!basePath.exists())
        {
            basePath.mkdirs();
        }

        WorkspaceManager wm = new WorkspaceManager(xmlPath);

        if (!xmlPath.exists())
        {
            Workspace workspace = new Workspace(xmlPath);
            wm.saveWorkspace(workspace);
        }

        return wm;
    }

    protected File xmlPath;
    protected Workspace workspace;

    public WorkspaceManager()
    {
        // FIXME Use Options.instance().getWorkspacePath()
        this(new File(
                new File(System.getProperty("user.home"), ".gitools"),
                WORKSPACE_FILE_NAME));
    }

    private WorkspaceManager(File xmlPath)
    {
        this.xmlPath = xmlPath;
    }

    public Workspace getWorkspace()
    {
        if (workspace == null)
        {
            if (!xmlPath.exists())
            {
                workspace = new Workspace(xmlPath);
                saveWorkspace(workspace);
            }
            else
            {
                try
                {
                    workspace = loadWorkspace(xmlPath);
                } catch (WorkspaceManagerException e)
                {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        return workspace;
    }

    public Workspace loadWorkspace(File file) throws WorkspaceManagerException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(Workspace.class);
            Unmarshaller u = context.createUnmarshaller();
            Workspace ws = (Workspace) u.unmarshal(new FileInputStream(file));
            ws.setXmlPath(file);
            return ws;
        } catch (Exception ex)
        {
            throw new WorkspaceManagerException(ex);
        }
    }

    public void createWorkspace(File path)
    {
        saveWorkspace(new Workspace(path));
    }

    public void saveWorkspace(Workspace workspace)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(Workspace.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File path = workspace.getXmlPath().getParentFile();
            if (!path.exists())
            {
                path.mkdirs();
            }

            m.marshal(workspace, new FileOutputStream(workspace.getXmlPath()));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Project createProject(File basePath, Project project) throws PersistenceException
    {
        if (!basePath.exists())
        {
            basePath.mkdirs();
        }
        File file = new File(basePath, FileSuffixes.PROJECT);
        IProgressMonitor monitor = new StreamProgressMonitor(System.out, true, true);
        PersistenceManager.get().store(new UrlResourceLocator(file), project, monitor);
        return project;
    }

    public Project createProject(File basePath) throws PersistenceException
    {
        return createProject(basePath, new Project());
    }

    //	private static WorkspaceManager instance;
    //
    //	private Workspace currentWorkspace;
    //
    //	private WorkspaceManager() {
    //	}
    //
    //	public static final WorkspaceManager getDefault() {
    //		if (instance == null)
    //			instance = new WorkspaceManager();
    //		return instance;
    //	}
    //
    //	public boolean exists(File path) {
    //		return path.exists() && path.isDirectory();
    //	}
    //
    //	public Workspace create(File path) {
    //		if (!path.exists())
    //			path.mkdirs();
    //
    //		Workspace workspace = new Workspace(path);
    //		return workspace;
    //	}
    //
    //	public Workspace open(File path) throws PersistenceException {
    //		if (!path.isDirectory())
    //			throw new RuntimeException("Illegal workspace: "
    //					+ path.getAbsolutePath());
    //
    //		Workspace workspace = new Workspace(path);
    //		loadProjects(workspace);
    //
    //		return workspace;
    //	}
    //
    //	private void loadProjects(Workspace workspace) throws PersistenceException {
    //		File[] resources = workspace.getPath().listFiles();
    //		for (File resource : resources) {
    //			if (resource.isDirectory()) {
    //				File projectResource = new File(resource, FileSuffixes.PROJECT);
    //				if (projectResource.exists()) {
    //					Project project = (Project) PersistenceManager.getDefault()
    //							.load(new FilePathResolver(), projectResource,
    //									null, new NullProgressMonitor());
    //					workspace.addProject(project);
    //				}
    //			}
    //		}
    //	}
    //
    //	public void close(Workspace workspace) {
    //		throw new RuntimeException("unimplemented");
    //	}
    //
    //	public void setCurrent(Workspace workspace) {
    //		this.currentWorkspace = workspace;
    //	}
    //
    //	public Workspace getCurrent() {
    //		return currentWorkspace;
    //	}

}
