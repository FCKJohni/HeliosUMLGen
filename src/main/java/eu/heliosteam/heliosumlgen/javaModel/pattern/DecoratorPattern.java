package eu.heliosteam.heliosumlgen.javaModel.pattern;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.Relation;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DecoratorPattern implements IPattern {
	
	public final Set<AbstractJavaStructure> set;
	public final AbstractJavaStructure struct;
	public static final String COMPONENT = "component";
	public static final String DECORATOR = "decorator";
	public static final String DECORATOR_NAME = "decorates";
	
	public DecoratorPattern(AbstractJavaStructure struct, Set<AbstractJavaStructure> set) {
		this.struct = struct;
		this.set = set;
	}
	
	@Override
	public String getStereotype(AbstractJavaStructure struct) {
		if (this.struct.equals(struct)) {
			return COMPONENT;
		}
		if (this.set.contains(struct)) {
			return DECORATOR;
		}
		return null;
	}
	
	public void addToDecorators(AbstractJavaStructure struct) {
		this.set.add(struct);
	}

	@Override
	public List<AbstractJavaStructure> getInvolvedStructes() {
		return new LinkedList<>(set);
	}

	@Override
	public Color getDefaultColor() {
		return Color.GREEN;
	}

	@Override
	public List<Relation> getTopLevelRelations() {
		List<Relation> toReturn = new LinkedList<>();
		
		for(AbstractJavaStructure struct: set) {
			if(struct.equals(this.struct))
				continue;
			boolean valid = true;
			for(AbstractJavaStructure suppers: struct.getSuperClasses()) {
				if(suppers.equals(struct)){
					continue;
				}
				if(!this.struct.equals(suppers) && set.contains(suppers)) {
					valid = false;
					break;
				}
			}
			if(valid)
				toReturn.add(new Relation(struct, this.struct));
		}
		
		return toReturn;
	}

	@Override
	public String getRelationName() {
		return DECORATOR_NAME;
	}

}
