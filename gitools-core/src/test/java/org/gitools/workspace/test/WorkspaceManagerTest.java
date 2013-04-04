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
package org.gitools.workspace.test;

import junit.framework.TestCase;
import org.gitools.model.Attribute;
import org.gitools.model.Laboratory;
import org.gitools.model.Project;
import org.gitools.model.Publication;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceManager;
import org.gitools.workspace.WorkspaceProjectRef;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class WorkspaceManagerTest extends TestCase
{

    //@Test
    public void testWorkspaceOpen()
    {
        File xmlPath = openResourceFile("workspace/" + FileSuffixes.WORKSPACE);
        WorkspaceManager wm = WorkspaceManager.createManager(xmlPath);
        Workspace ws = wm.getWorkspace();
        assertNotNull(ws);
    }

    //@Test
    public void testProjectCreate() throws IOException, PersistenceException
    {
        if (true)
        {
            return;
        }

        // Create temporary workspace
        File basePath = File.createTempFile("gitools-workspace-", "");
        basePath.delete();
        basePath.mkdirs();
        File xmlPath = new File(basePath, "workspace.xml");
        WorkspaceManager wm = WorkspaceManager.createManager(xmlPath);
        Workspace ws = wm.getWorkspace();

        // Create project entity
        File projPath = File.createTempFile("project-", "", basePath);
        projPath.delete();
        projPath.mkdirs();
        Project project = new Project();
        project.setTitle("Test project 2");
        project.setDescription(
                "This project has been created to serve as a test project");

        project.setLaboratories(Arrays.asList(new Laboratory[]{
                new Laboratory("Lab1", "http://www.lab1.com"),
                new Laboratory("Lab2", "http://www.lab2.com")}));

        project.setAttributes(Arrays.asList(new Attribute[]{
                new Attribute("attr1", "value1"),
                new Attribute("attr2", "value2")}));

        project.setPublications(Arrays.asList(new Publication[]{
                new Publication("Publication 1", "Fu Lee, X", "This paper analyzes...", "Super Journal", "000001", "http://papers.com/000001.pdf"),
                new Publication("Publication 2", "Fu Lee, X et all", "This paper reveals...", "Super Journal", "000002", "http://papers.com/000002.pdf")}));

        // Create project
        wm.createProject(projPath, project);

        // Add to workspace
        WorkspaceProjectRef projectRef = new WorkspaceProjectRef(ws);
        projectRef.setUrl(projPath.getName());
        ws.addProjectRef(projectRef);
        WorkspaceManager.getDefault().saveWorkspace(ws);
    }

    @Nullable
    protected File openResourceFile(String resourceName)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(resourceName);
        URL resource = classLoader.getResource(resourceName);
        URI uri = null;
        try
        {
            uri = resource.toURI();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return uri != null ? new File(uri) : null;
    }
}
