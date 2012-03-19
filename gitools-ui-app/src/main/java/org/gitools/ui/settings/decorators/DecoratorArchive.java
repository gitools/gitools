/*
 *  Copyright 2012 michi.
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

package org.gitools.ui.settings.decorators;

import java.util.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;


/**
 *
 * @author michi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DecoratorArchive {

    public transient final static String VERSION = "2.0";

    private List<ElementDecorator> scaleDecorators = new ArrayList<ElementDecorator>();
        
    private transient final String DEFAULT = "Default";

    public DecoratorArchive() {
    }

    public void add(ElementDecorator[] decorators) {
        for (ElementDecorator d : decorators) {
            add(d);
        }
    }
    
    public void add(ElementDecorator decorator) {
        String name = decorator.getName();
        Set<String> map = getDecorators().keySet();
        if (map.contains(name)) {
            remove(scaleDecorators,name);
            scaleDecorators.add(decorator);
        } else {
            scaleDecorators.add(decorator);
        }
    }
    
    private void remove(List<ElementDecorator> scaleDecorators, String name) {
           for(ElementDecorator d : scaleDecorators) {
               if (d.getName().equals(name)) {
                   scaleDecorators.remove(d);
                   return;
               }
           }
    }
    
    public Map<String, ElementDecorator> getDecorators(){
        Map<String, ElementDecorator> decoratorMap = new HashMap<String, ElementDecorator>();
        
        for (ElementDecorator d : scaleDecorators) {
            decoratorMap.put(d.getName(), d);
        }
        return decoratorMap;
    }
   
    public ElementDecorator[] getDefaultElementDecoratros() {
        List<ElementDecoratorDescriptor> descriptors = ElementDecoratorFactory.getDescriptors();
        ElementDecorator[] decorators = new ElementDecorator[descriptors.size()];
        int i = 0;
        for (ElementDecoratorDescriptor descriptor : descriptors) {
            ElementDecorator decorator = null;
            try {
                decorator = descriptor.getDecoratorClass().
                        getConstructor().newInstance();
            }
            catch (Exception e) {
            	return null;
            }
            //decorator = (descriptor.getDecoratorClass()) decorator;
            decorator.setName(DEFAULT + " " +descriptor.getName());
            decorators[i] = decorator;
            i++;
        }
        return decorators;
    }
}
