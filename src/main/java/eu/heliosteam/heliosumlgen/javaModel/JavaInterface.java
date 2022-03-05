package eu.heliosteam.heliosumlgen.javaModel;


import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;

import java.io.IOException;

public class JavaInterface extends AbstractJavaStructure {

	public JavaInterface(String name) {
		super(name);
	}

	@Override
	public void accept(IUMLVisitor v) throws IOException {
		v.visit(this);

		super.accept(v);
	}
}
