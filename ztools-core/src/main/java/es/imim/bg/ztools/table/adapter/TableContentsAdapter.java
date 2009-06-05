package es.imim.bg.ztools.table.adapter;

import java.io.Serializable;

import es.imim.bg.progressmonitor.DefaultProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.resources.IResource;
import es.imim.bg.ztools.table.ITableContents;

public abstract class TableContentsAdapter<T> implements ITableContents,
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
