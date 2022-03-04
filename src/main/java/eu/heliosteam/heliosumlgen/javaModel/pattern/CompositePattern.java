package eu.heliosteam.heliosumlgen.javaModel.pattern;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.Relation;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CompositePattern implements IPattern {

    public static final String COMPONENT = "Component";
    public static final String LEAF = "Leaf";
    public static final String COMPOSITE = "Composite";
    public final AbstractJavaStructure component;
    final Set<AbstractJavaStructure> composites;
    final Set<AbstractJavaStructure> leaves;

    public CompositePattern(AbstractJavaStructure component) {
        this.component = component;
        this.composites = new HashSet<>();
        this.leaves = new HashSet<>();
    }

    @Override
    public String getStereotype(AbstractJavaStructure struct) {
        if (component.equals(struct))
            return COMPONENT;
        if (composites.contains(struct))
            return COMPOSITE;
        if (leaves.contains(struct))
            return LEAF;

        return null;
    }

    @Override
    public List<AbstractJavaStructure> getInvolvedStructes() {
        List<AbstractJavaStructure> toReturn = new LinkedList<>();

        toReturn.addAll(leaves);
        toReturn.addAll(composites);
        toReturn.add(component);

        return toReturn;
    }

    @Override
    public Color getDefaultColor() {
        return Color.YELLOW;
    }

    @Override
    public List<Relation> getTopLevelRelations() {
        return new LinkedList<>();
    }

    @Override
    public String getRelationName() {
        return "";
    }

    public void addLeaf(AbstractJavaStructure struct) {
        this.leaves.add(struct);
    }

    public void addComposite(AbstractJavaStructure struct) {
        this.composites.add(struct);
    }

}
