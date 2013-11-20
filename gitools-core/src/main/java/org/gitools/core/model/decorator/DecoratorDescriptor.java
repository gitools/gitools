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
package org.gitools.core.model.decorator;

public class DecoratorDescriptor {

    private String name;
    private Class<? extends Decorator> decoratorClass;

    public DecoratorDescriptor(String name, Class<? extends Decorator> decoratorClass) {
        this.name = name;
        this.decoratorClass = decoratorClass;
    }

    public final String getName() {
        return name;
    }

    public final Class<? extends Decorator> getDecoratorClass() {
        return decoratorClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof DecoratorDescriptor)) {
            return false;
        }
        DecoratorDescriptor other = (DecoratorDescriptor) obj;
        return name.equals(other.name) && decoratorClass.equals(other.decoratorClass);
    }

    @Override
    public int hashCode() {
        return 17 * name.hashCode() + decoratorClass.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
