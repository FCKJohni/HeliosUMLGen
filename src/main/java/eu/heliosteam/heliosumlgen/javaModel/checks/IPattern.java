package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.Relation;

import java.awt.*;
import java.util.List;

public interface IPattern {
	public String getStereotype(AbstractJavaStructure struct);
	public List<AbstractJavaStructure> getInvolvedStructes();
	public Color getDefaultColor();
	public List<Relation> getTopLevelRelations();
	String getRelationName();
}
