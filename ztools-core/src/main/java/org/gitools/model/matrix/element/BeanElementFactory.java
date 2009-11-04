package org.gitools.model.matrix.element;


public class BeanElementFactory implements IElementFactory {

	protected Class<?> elementClass;
	
	public BeanElementFactory(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	@Override
	public Object create() {
		Object object = null;
		try {
			object = elementClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

}
