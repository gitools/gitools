/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.tools;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "tools")
public class ToolSet {

    private List<ToolDescriptor> toolDescriptors = new ArrayList<ToolDescriptor>();

    public ToolSet() {
    }

    @XmlElement(name = "tool")
    public List<ToolDescriptor> getToolDescriptors() {
        return toolDescriptors;
    }

    public void setToolDescriptors(List<ToolDescriptor> toolDescriptors) {
        this.toolDescriptors = toolDescriptors;
    }

    public void add(ToolSet toolSet) {
        toolDescriptors.addAll(toolSet.getToolDescriptors());
    }

    public void add(ToolDescriptor desc) {
        toolDescriptors.add(desc);
    }
}
