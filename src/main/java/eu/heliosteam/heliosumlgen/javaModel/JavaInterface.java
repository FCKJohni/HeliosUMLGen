package eu.heliosteam.heliosumlgen.javaModel;


import eu.heliosteam.heliosumlgen.javaModel.modifier.IAccessModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;

import java.io.IOException;
import java.util.List;

public class JavaInterface extends AbstractJavaStructure {

	public JavaInterface(String name, IAccessModifier access, List<IModifier> modifiers,
						 List<AbstractJavaElement> subElements, List<AbstractJavaStructure> implement) {
		super(name, access, modifiers, subElements, implement);
	}

	public JavaInterface(String name) {
		super(name);
	}

	@Override
	public void accept(IUMLVisitor v) throws IOException {
		v.visit(this);

		super.accept(v);
	}
}
