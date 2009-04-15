package es.imim.bg.ztools.table.element;
		
public interface IElementProperty {

	String getId();
	String getName();
	String getDescription();
	
	Class<?> getValueClass();
	
}
