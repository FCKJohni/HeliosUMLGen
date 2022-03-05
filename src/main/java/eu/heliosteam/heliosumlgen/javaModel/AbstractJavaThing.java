package eu.heliosteam.heliosumlgen.javaModel;

import eu.heliosteam.heliosumlgen.javaModel.modifier.IAccessModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLTraverser;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractJavaThing implements IUMLTraverser {
	

	public final String name;
	public IAccessModifier access;
	public List<IModifier> modifiers;

	public AbstractJavaThing(String name, IAccessModifier access, List<IModifier> modifiers) {
		this.name = name;
		this.access = access;
		this.modifiers = modifiers;
	}

	public AbstractJavaThing(String cleanName) {
		this(cleanName, null, new LinkedList<>());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractJavaThing other = (AbstractJavaThing) obj;
		if (name == null) {
			return other.name == null;
		} else return name.equals(other.name);
	}
}
