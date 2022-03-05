package eu.heliosteam.heliosumlgen.javaModel;

import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JavaClass extends AbstractJavaStructure {
	public AbstractJavaStructure superClass;



	public JavaClass(String cleanName) {
		super(cleanName);

		this.superClass = null;
	}

	@Override
	public void accept(IUMLVisitor v) throws IOException {
		v.visit(this);

		super.accept(v);
	}

	@Override
	public boolean isCastableTo(AbstractJavaStructure struct) {
		if(super.isCastableTo(struct))
			return true;

		return superClass != null && superClass.isCastableTo(struct);
	}
	
	@Override
	protected void getSuperClasses(Set<AbstractJavaStructure> set) {
		if(this.superClass != null)
			this.superClass.getSuperClasses(set);
		super.getSuperClasses(set);
	}
	
	@Override
	public List<AbstractJavaElement> getElementByName(String name) {
		List<AbstractJavaElement> element = super.getElementByName(name);
		
		if(superClass != null)
			element.addAll(superClass.getElementByName(name));
		
		return element;
	}
	
	@Override
	protected <T extends AbstractJavaElement> void getElementsOfType(List<T> list, Class<T> clazz) {
		super.getElementsOfType(list, clazz);
		
		if(superClass != null) {
			superClass.getElementsOfType(list, clazz);
		}
	}
}
