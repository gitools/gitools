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
		getListeners().add(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getListeners().remove(listener);
	}
	
	protected void firePropertyChange(String propName) {
		firePropertyChange(propName, null, null);
	}
	
	protected void firePropertyChange(
			String propName, Object oldValue, Object newValue) {
		
		for (PropertyChangeListener l : getListeners()) {
			PropertyChangeEvent evt = 
				new PropertyChangeEvent(this, propName, oldValue, newValue);
			l.propertyChange(evt);
		}
	}
}
