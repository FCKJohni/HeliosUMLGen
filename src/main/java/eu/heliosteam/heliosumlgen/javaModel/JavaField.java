package eu.heliosteam.heliosumlgen.javaModel;


import eu.heliosteam.heliosumlgen.javaModel.modifier.IAccessModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;

import java.io.IOException;
import java.util.List;

public class JavaField extends AbstractJavaElement {

	public JavaField(AbstractJavaStructure owner, String name, IAccessModifier access, List<IModifier> modifiers, AbstractJavaStructure type) {
		super(owner, name, access, modifiers, type);
	}

	@Override
	public void accept(IUMLVisitor v) throws IOException {
		v.visit(this);
	}
}
