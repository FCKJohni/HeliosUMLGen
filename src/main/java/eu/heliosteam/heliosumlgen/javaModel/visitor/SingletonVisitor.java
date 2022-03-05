package eu.heliosteam.heliosumlgen.javaModel.visitor;

import eu.heliosteam.heliosumlgen.JsonConfig;
import eu.heliosteam.heliosumlgen.asm.QualifiedMethod;
import eu.heliosteam.heliosumlgen.asm.Utils;
import eu.heliosteam.heliosumlgen.javaModel.*;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.StaticModifier;
import eu.heliosteam.heliosumlgen.javaModel.pattern.SingletonPattern;

import java.util.LinkedList;
import java.util.List;

public class SingletonVisitor implements IStructureVisitor {
	
	private boolean requireGetInstance = true;
	

	@Override
	public List<IPattern> visit(JavaModel model, AbstractJavaStructure struct) {
		List<IPattern> toReturn = new LinkedList<>();
		
		if(struct instanceof JavaInterface) {
			// Do nothing
		}
		else if(checkForStaticInstance(struct) && !requireGetInstance){
			toReturn.add(new SingletonPattern((JavaClass)struct));
		}else if(checkForGetInstance(model, struct)) {
			toReturn.add(new SingletonPattern((JavaClass)struct));
		}
		
		return toReturn;
	}
	
	private boolean checkForStaticInstance(AbstractJavaStructure structure) {
		for(AbstractJavaElement element: structure.subElements) {
			if(element.name.equalsIgnoreCase("instance"))
				if(checkForModifier(element.modifiers))
					return true;
		}
		return false;
	}
	
	private boolean checkForModifier(List<IModifier> modifiers) {
		for(IModifier mod: modifiers)
			if(StaticModifier.class.isInstance(mod))
				return true;
		
		return false;
	}

	private boolean checkForGetInstance(JavaModel model, AbstractJavaStructure structure) {
		JavaMethod method = structure.getMethodByQualifiedName(new QualifiedMethod("getInstance", "()" + Utils.getAsmName(structure.name)), model);
		
		if(method == null)
			method = structure.getMethodByQualifiedName(new QualifiedMethod("get" + Utils.shortName(structure.name), "()" + Utils.getAsmName(structure.name)), model);
		
		if(method == null)
			return false;
		
		if(method.arguments != null && method.arguments.size() != 0)
			return false;
		
		if(!checkForModifier(method.modifiers))
			return false;

		return method.type.equals(structure);
	}

	@Override
	public void setSettings(JsonConfig config) {
		requireGetInstance = config.Singleton_RequireGetInstance;
	}
}
