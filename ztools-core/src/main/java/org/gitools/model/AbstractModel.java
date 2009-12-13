package org.gitools.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractModel 
		implements IModel, Serializable {

	private static final long serialVersionUID = -8918954049958104274L;

	public static final String PROPERTY_CHANGED = "propertyChanged";
	
	protected ArrayList<PropertyChangeListener> listeners =
		new ArrayList<PropertyChangeListener>(0);
	
	public AbstractModel() {
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void firePropertyChange(String propName) {
		firePropertyChange(propName, null, null);
	}
	
	protected void firePropertyChange(
			String propName, Object oldValue, Object newValue) {
		
		for (PropertyChangeListener l : listeners) {
			PropertyChangeEvent evt = 
				new PropertyChangeEvent(this, propName, oldValue, newValue);
			l.propertyChange(evt);
		}
	}
}
