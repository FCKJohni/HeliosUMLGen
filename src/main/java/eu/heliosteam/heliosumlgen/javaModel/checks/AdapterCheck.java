package eu.heliosteam.heliosumlgen.javaModel.checks;


import eu.heliosteam.heliosumlgen.javaModel.*;
import eu.heliosteam.heliosumlgen.javaModel.pattern.AdapterPattern;

import java.util.LinkedList;
import java.util.List;

public class AdapterCheck implements IPatternCheck {


	@Override
	public List<IPattern> check(JavaModel model) {
		List<IPattern> toReturn = new LinkedList<>();
		
		for(AbstractJavaStructure struct: model.getStructures()) {
			if( !(struct instanceof JavaClass))
				continue;
			
			AbstractJavaStructure to = checkInheritance((JavaClass)struct);
			if(to == null) {
				continue;
			}
			
			for(JavaMethod meth: struct.getConstructors() ) {
				if(meth.arguments.size() > 1) 
					break;
				for(AbstractJavaStructure arg: meth.arguments) {
					if(hasFieldCatableTo(struct.subElements, arg)) {
						if(checkMethodsForCallTo(struct.subElements, arg) && ! (to.isCastableTo(arg) || arg.isCastableTo(to))) {
							toReturn.add(new AdapterPattern(arg, to, (JavaClass) struct));
						}
					}
				}
			}
		}
		
		return toReturn;
	}
	
	private boolean checkMethodsForCallTo(List<AbstractJavaElement> subElements, AbstractJavaStructure arg) {

		for(AbstractJavaElement ele: subElements) {
			if(ele instanceof JavaMethod) {
				if(((JavaMethod) ele).isConstructor)
					continue;
				for(JavaMethod meth :((JavaMethod) ele).methodCalls) {
					if(meth.owner.equals(arg)) {
						break;
					}
				}
			}
		}
		return true;
	}

	public boolean hasFieldCatableTo(List<AbstractJavaElement> elements, AbstractJavaStructure to) {
		for(AbstractJavaElement ele: elements) {
			if(ele instanceof JavaField && ele.type.isCastableTo(to)) {
				return true;
			}
		}
		return false;
	}
	
	public AbstractJavaStructure checkInheritance(JavaClass struct) {
		AbstractJavaStructure implments = null;
		AbstractJavaStructure supers = null;
		
		if(struct.implement != null && struct.implement.size() == 1) {
			implments = struct.implement.get(0);
		}
		
		if(struct.superClass != null) {
			if(!struct.superClass.name.equals("java.lang.Object")){
				supers = struct.superClass;				
			}
		}

		if(implments != null && supers != null || implments == null && supers == null) {
			return null;
		}
		
		return supers == null ? implments : supers;
	}

}
