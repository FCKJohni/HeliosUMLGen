package eu.heliosteam.heliosumlgen.javaModel;

import eu.heliosteam.heliosumlgen.asm.QualifiedMethod;
import eu.heliosteam.heliosumlgen.asm.Utils;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class AbstractJavaStructure extends AbstractJavaThing {
	public final List<AbstractJavaElement> subElements;
	public List<AbstractJavaStructure> implement;



	public AbstractJavaStructure(String cleanName) {
		super(cleanName);
		this.subElements = new LinkedList<>();
	}

	public void addSubElement(AbstractJavaElement element) {
		this.subElements.add(element);
	}

	public void accept(IUMLVisitor v) throws IOException {

		for (AbstractJavaElement element : subElements) {
			if (element instanceof JavaField)
				v.visit((JavaField) element);
		}

		v.visitEndFields();

		for (AbstractJavaElement element : subElements) {
			if (element instanceof JavaMethod)
				v.visit((JavaMethod) element);
		}

		v.visitEndStructure();
	}
	
	public List<AbstractJavaElement> getElementByName(String name) {
		List<AbstractJavaElement> toReturn = new LinkedList<>();
		for(AbstractJavaElement element: this.subElements) {
			if(element.name.equals(name)){
				toReturn.add(element);
			}
		}
		return toReturn;
	}
	
	public JavaMethod getMethodByQualifiedName(QualifiedMethod meth, JavaModel model) {
		for(AbstractJavaElement element: this.subElements) {
			if (element instanceof JavaMethod) {
				if(element.name.equals(meth.methodName)){
					List<String> list = Utils.getListOfArgs(meth.methodDesc);
					JavaMethod method = (JavaMethod)element;
					if (list.size() != method.arguments.size()) {
						continue;
					}
					boolean t = true;
					for (int i = 0; i < list.size(); i++) {
						AbstractJavaStructure struct = Utils.getInstanceOrJavaStructure(model, list.get(i));
						if (!struct.equals(method.arguments.get(i))){
							t = false;
							break;
						}
					}
					if (!t) {
						continue;
					}
					return method;
				}
			}
		}
		
		return null;
	}
	
	public List<JavaMethod> getConstructors() {
		List<JavaMethod> toReturn = new LinkedList<>();
		for(AbstractJavaElement ele: subElements) {
			if(ele instanceof JavaMethod) {
				if(((JavaMethod)ele).isConstructor) {
					toReturn.add((JavaMethod)ele);
				}
			}
		}
		return toReturn;
	}
	
	public boolean isCastableTo(AbstractJavaStructure struct) {
		if(this.equals(struct)) {
			return true;
		}
		else {
			if(this.implement != null)
				for(AbstractJavaStructure imp: this.implement) {
					if(imp.isCastableTo(struct))
						return true;
				}
		}
		
		return false;
	}
	
	public Set<AbstractJavaStructure> getSuperClasses() {
		Set<AbstractJavaStructure> toReturn = new HashSet<>();
		getSuperClasses(toReturn);
		return toReturn;
	}
	
	protected void getSuperClasses(Set<AbstractJavaStructure> set) {
		set.add(this);
		if(this.implement != null)
			for (AbstractJavaStructure struct: this.implement) {
				struct.getSuperClasses(set);
			}
	}

	// This is checked via isInstance
	@SuppressWarnings("unchecked")
	protected <T extends AbstractJavaElement> void getElementsOfType(List<T> list, Class<T> clazz) {
		for(AbstractJavaElement ele: subElements) {
			if(clazz.isInstance(ele)) {
				list.add((T) ele);
			}
		}
		if(this.implement != null) {
			for(AbstractJavaStructure struct: implement) {
				struct.getElementsOfType(list, clazz);
			}
		}
	}
}
