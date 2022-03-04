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

    public static final String STEREOTYPE = "singleton";
    final JavaClass struct;

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
        List<AbstractJavaStructure> toReturn = new ArrayList<>(1);
        toReturn.add(struct);
        return toReturn;
    }

    @Override
    public Color getDefaultColor() {
        return Color.BLUE;
    }

    @Override
    public List<Relation> getTopLevelRelations() {
        return new LinkedList<>();
    }

    @Override
    public String getRelationName() {
        return "";
    }

}
