/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.settings.decorators;

import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.model.decorator.DecoratorDescriptor;
import org.gitools.core.model.decorator.DecoratorFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DecoratorArchive {

    public transient final static String VERSION = "3.0";


    private final List<Decorator> scaleDecorators = new ArrayList<>();

    private transient final String DEFAULT = "Default";

    public DecoratorArchive() {
    }

    public void add(Decorator[] decorators) {
        for (Decorator d : decorators) {
            add(d);
        }
    }

    public void add(Decorator decorator) {
        String name = decorator.getName();
        Set<String> map = getDecorators().keySet();
        if (map.contains(name)) {
            remove(scaleDecorators, name);
            scaleDecorators.add(decorator);
        } else {
            scaleDecorators.add(decorator);
        }
    }

    private void remove(List<Decorator> scaleDecorators, String name) {
        for (Decorator d : scaleDecorators) {
            if (d.getName().equals(name)) {
                scaleDecorators.remove(d);
                return;
            }
        }
    }


    public Map<String, Decorator> getDecorators() {
        Map<String, Decorator> decoratorMap = new HashMap<>();

        for (Decorator d : scaleDecorators) {
            decoratorMap.put(d.getName(), d);
        }
        return decoratorMap;
    }


    public Decorator[] getDefaultElementDecoratros() {
        List<DecoratorDescriptor> descriptors = DecoratorFactory.getDescriptors();
        Decorator[] decorators = new Decorator[descriptors.size()];
        int i = 0;
        for (DecoratorDescriptor descriptor : descriptors) {
            Decorator decorator = null;
            try {
                decorator = descriptor.getDecoratorClass().
                        getConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
            //decorator = (descriptor.getDecoratorClass()) decorator;
            decorator.setName(DEFAULT + " " + descriptor.getName());
            decorators[i] = decorator;
            i++;
        }
        return decorators;
    }
}
