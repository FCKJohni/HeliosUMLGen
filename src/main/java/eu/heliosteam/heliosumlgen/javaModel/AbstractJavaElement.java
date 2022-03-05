package eu.heliosteam.heliosumlgen.javaModel;


import eu.heliosteam.heliosumlgen.javaModel.modifier.IAccessModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;

import java.util.List;

public abstract class AbstractJavaElement extends AbstractJavaThing {
	
	public AbstractJavaStructure owner;
	public AbstractJavaStructure type;
	

	public AbstractJavaElement(AbstractJavaStructure owner, String name, IAccessModifier access, List<IModifier> modifiers,
							   AbstractJavaStructure type) {
		super(name, access, modifiers);
		this.owner = owner;
		this.type = type;
	}
}
