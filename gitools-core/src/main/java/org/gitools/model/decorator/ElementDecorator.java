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

package org.gitools.model.decorator;

import org.gitools.utils.colorscale.IColorScale;
import javax.xml.bind.annotation.*;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.impl.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({
	BinaryElementDecorator.class,
	FormattedTextElementDecorator.class,
	LinearTwoSidedElementDecorator.class,
	PValueElementDecorator.class,
	ZScoreElementDecorator.class,
    CorrelationElementDecorator.class
})
public abstract class ElementDecorator extends AbstractModel {

	private static final long serialVersionUID = -2101303088018509837L;

	@XmlTransient
	protected IElementAdapter adapter;

	private int valueIndex;
    
    @XmlAttribute
    private String name;
	
	public ElementDecorator() {
	}
	
	public ElementDecorator(IElementAdapter adapter) {
		this.adapter = adapter;
	}

    public ElementDecorator(IColorScale scale, IElementAdapter adapter) {
        this.adapter = adapter;
    }
	
	public IElementAdapter getAdapter() {
		return adapter;
	}

	public abstract void setValueIndex(int valueIndex);

	public int getValueIndex() {
		return this.valueIndex;
	}
	
	public void setAdapter(IElementAdapter adapter){
		this.adapter = adapter;
	}
	
	public abstract void decorate(
			ElementDecoration decoration, 
			Object element);

	public abstract IColorScale getScale();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*	@Deprecated //FIXME use JAXB to save state
	public Map<String, String> getConfiguration() {
		return null;
	}

	@Deprecated
	public abstract void setConfiguration(Map<String, String> configuration);*/
	
	protected int getPropertyIndex(String[] names) {
		int index = -1;
		int nameIndex = 0;
		
		while (index == -1 && nameIndex < names.length) {
			try {
				index = adapter.getPropertyIndex(names[nameIndex++]);
			}
			catch (Exception e) {}
		}
		
		if (index == -1)
			index = 0;
		
		return index;
	}
}
