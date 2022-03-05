package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.Relation;

import java.awt.*;
import java.util.List;

public interface IPattern {
	String getStereotype(AbstractJavaStructure struct);
	List<AbstractJavaStructure> getInvolvedStructes();
	Color getDefaultColor();
	List<Relation> getTopLevelRelations();
	String getRelationName();
}
