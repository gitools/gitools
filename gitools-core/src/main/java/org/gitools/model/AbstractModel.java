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

package org.gitools.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractModel 
		implements IModel, Serializable, Cloneable {

	private static final long serialVersionUID = -8918954049958104274L;

	public static final String PROPERTY_CHANGED = "propertyChanged";
	
	private transient ArrayList<PropertyChangeListener> listeners;
	
	public AbstractModel() {
	}

	public ArrayList<PropertyChangeListener> getListeners() {
		if (listeners == null)
			listeners = new ArrayList<PropertyChangeListener>(0);
		return listeners;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener != null)
			getListeners().add(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener != null)
			getListeners().remove(listener);
	}
	
	protected void firePropertyChange(String propName) {
		//firePropertyChange(propName, null, null);

		//System.out.println(new Date().toString() + " " + getClass().getSimpleName() + ": " + propName);

		for (PropertyChangeListener l : getListeners()) {
			PropertyChangeEvent evt =
				new PropertyChangeEvent(this, propName, null, null);
			l.propertyChange(evt);
		}
	}
	
	protected void firePropertyChange(
			String propName, Object oldValue, Object newValue) {

		if ((oldValue != null && !oldValue.equals(newValue)) ||
				(oldValue == null && newValue != null)) {

			//System.out.println("\nPropertyChange: " + propName + " " + oldValue + " -> " + newValue + " Class: " + this);
			for (PropertyChangeListener l : getListeners()) {
				PropertyChangeEvent evt =
					new PropertyChangeEvent(this, propName, oldValue, newValue);
				//System.out.println("  >>> " + l);
				l.propertyChange(evt);
			}
		}
	}
}
