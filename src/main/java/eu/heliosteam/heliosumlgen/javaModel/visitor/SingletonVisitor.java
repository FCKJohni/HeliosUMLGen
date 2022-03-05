package eu.heliosteam.heliosumlgen.javaModel.visitor;

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


	@Override
	public List<IPattern> visit(JavaModel model, AbstractJavaStructure struct) {
		List<IPattern> toReturn = new LinkedList<>();

		boolean requireGetInstance = true;
		if(struct instanceof JavaInterface) {
			// Do nothing
		}
		else {
			checkForStaticInstance(struct);
			if(checkForGetInstance(model, struct)) {
				toReturn.add(new SingletonPattern((JavaClass)struct));
			}
		}
		
		return toReturn;
	}
	
	private void checkForStaticInstance(AbstractJavaStructure structure) {
		for(AbstractJavaElement element: structure.subElements) {
			if(element.name.equalsIgnoreCase("instance"))
				if(checkForModifier(element.modifiers))
					return;
		}
	}
	
	private boolean checkForModifier(List<IModifier> modifiers) {
		for(IModifier mod: modifiers)
			if(mod instanceof StaticModifier)
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

}
