package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.javaModel.*;
import eu.heliosteam.heliosumlgen.javaModel.pattern.CompositePattern;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CompositeCheck implements IPatternCheck {

    final Set<AbstractJavaStructure> set = new HashSet<>();

    @Override
    public List<IPattern> check(JavaModel model) {
        List<IPattern> toReturn = new LinkedList<>();

        List<AbstractJavaStructure> leafPossibility = new LinkedList<>();

        for (AbstractJavaStructure struct : model.getStructures()) {
            set.clear();
            if (hasAddRemove(struct) /* && checkForCollection(struct, model) */) {
                for (AbstractJavaStructure component : set) {
                    CompositePattern pattern = containsPattern(toReturn, component);
                    if (pattern != null) {
                        pattern.addComposite(struct);
                    } else {
                        pattern = new CompositePattern(component);
                        pattern.addComposite(struct);

                        for (AbstractJavaStructure leaf : leafPossibility) {
                            if (leaf.isCastableTo(component)) {
                                pattern.addComposite(leaf);
                            }
                        }
                        toReturn.add(pattern);
                    }
                }
            } else {
                for (IPattern pattern : toReturn) {
                    AbstractJavaStructure comp = ((CompositePattern) pattern).component;
                    if (struct.isCastableTo(comp)) {
                        ((CompositePattern) pattern).addLeaf(struct);
                    }
                }
                leafPossibility.add(struct);
            }
        }

        return toReturn;
    }

    public boolean hasAddRemove(AbstractJavaStructure struct) {
        List<AbstractJavaElement> addList = struct.getElementByName("add");
        List<AbstractJavaElement> removeList = struct.getElementByName("remove");

        Set<AbstractJavaStructure> castedTo = new HashSet<>();

        boolean passing = false;
        for (AbstractJavaElement add : addList) {
            if (add instanceof JavaMethod && ((JavaMethod) add).arguments.size() == 1) {
                passing = true;
                castedTo.add(((JavaMethod) add).arguments.get(0));
            }
        }
        if (!passing)
            return false;

        for (AbstractJavaElement remove : removeList) {
            if (remove instanceof JavaMethod && ((JavaMethod) remove).arguments.size() == 1) {
                passing = true;
                break;
            }
        }
        if (!passing)
            return false;

        passing = false;
        for (AbstractJavaStructure otherStruct : castedTo) {
            if (struct.isCastableTo(otherStruct)) {
                set.add(otherStruct);
                passing = true;
            }
        }

        return passing;

    }

    public CompositePattern containsPattern(List<IPattern> patterns, AbstractJavaStructure to) {
        for (IPattern pattern : patterns) {
            CompositePattern dPattern = (CompositePattern) pattern;
            if (dPattern.component.equals(to)) {
                return dPattern;
            }
        }
        return null;
    }

}
