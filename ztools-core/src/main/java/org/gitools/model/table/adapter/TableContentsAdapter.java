package org.gitools.model.table.adapter;

import java.io.Serializable;

import org.gitools.model.table.IMatrix;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.ProgressMonitor;

public abstract class TableContentsAdapter<T> implements IMatrix,
		Serializable {

	private IResource<T> resource;
	private transient T data;

	public TableContentsAdapter(T data) {
		this.data = data;
	}

	public TableContentsAdapter(T data, IResource<T> resource) {
		this.data = data;
		this.resource = resource;
	}

	protected T getMatrix() {
		if (data == null) {
			
			if (resource == null)
				return null;
			try {
				// FIXME:
				// not here the progressMonitor

				ProgressMonitor monitor = new DefaultProgressMonitor();
				monitor.begin("Loading Data loosed from serialization...", 1);
				data = resource.load(monitor);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}

}
