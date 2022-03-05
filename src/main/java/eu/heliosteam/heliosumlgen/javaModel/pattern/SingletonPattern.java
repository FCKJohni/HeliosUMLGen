package eu.heliosteam.heliosumlgen.javaModel.pattern;


import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.JavaClass;
import eu.heliosteam.heliosumlgen.javaModel.Relation;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SingletonPattern implements IPattern {

	JavaClass struct;
	public static final String STEREOTYPE = "singleton";
	
	public SingletonPattern(JavaClass struct) {
		this.struct = struct;
	}
	
	@Override
	public String getStereotype(AbstractJavaStructure struct) {
		if (this.struct.equals(struct)) {
			return STEREOTYPE;
		}
		return null;
	}

	@Override
	public List<AbstractJavaStructure> getInvolvedStructes() {
		List<AbstractJavaStructure> toReturn = new ArrayList<AbstractJavaStructure>(1);
		toReturn.add(struct);
		return toReturn;
	}

	@Override
	public Color getDefaultColor() {
		return Color.BLUE;
	}

	@Override
	public List<Relation> getTopLevelRelations() {
		return new LinkedList<Relation>();
	}

	@Override
	public String getRelationName() {
		return "";
	}

}
