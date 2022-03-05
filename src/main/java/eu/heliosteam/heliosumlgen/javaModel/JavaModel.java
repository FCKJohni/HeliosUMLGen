package eu.heliosteam.heliosumlgen.javaModel;


import eu.heliosteam.heliosumlgen.HeliosLogger;
import eu.heliosteam.heliosumlgen.asm.MethodCallGroup;
import eu.heliosteam.heliosumlgen.asm.MethodCallLine;
import eu.heliosteam.heliosumlgen.asm.Utils;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPatternCheck;
import eu.heliosteam.heliosumlgen.javaModel.modifier.PublicModifier;
import eu.heliosteam.heliosumlgen.javaModel.visitor.*;

import java.io.IOException;
import java.util.*;

public class JavaModel {

    final HashMap<String, AbstractJavaStructure> map;
    final List<MethodCallGroup> methodGroups;
    final Set<String> includedClasses;

    final List<IPattern> patterns;

    public JavaModel(Set<String> includedClasses) {
        this.map = new HashMap<>();

        this.includedClasses = includedClasses;

        this.methodGroups = new LinkedList<>();

        this.patterns = new LinkedList<>();
    }

    public boolean containsStructure(String name) {
        return map.containsKey(name);
    }

    public void putStructure(String name, AbstractJavaStructure struct) {
        map.put(name, struct);
    }

    public AbstractJavaStructure getStructure(String name) {
        return map.get(name);
    }

    public List<Relation> getChildParrentIncludedRelations() {
        List<Relation> relations = new LinkedList<>();
        for (AbstractJavaStructure struct : map.values()) {
            if (struct instanceof JavaClass) {
                if (isStructureIncluded(struct.name)) {
                    JavaClass clazz = (JavaClass) struct;
                    if (clazz.superClass != null) {
                        if (isStructureIncluded(clazz.superClass.name)) {
                            relations.add(new Relation(clazz, clazz.superClass));
                        }
                    }
                }
            }
        }
        return relations;
    }

    public List<Relation> getIncludedInterfaceRelations() {
        List<Relation> relations = new LinkedList<>();
        for (AbstractJavaStructure struct : map.values()) {
            if (isStructureIncluded(struct.name)) {
                for (AbstractJavaStructure other : struct.implement) {
                    if (isStructureIncluded(other.name)) {
                        relations.add(new Relation(struct, other));
                    }
                }
            }
        }
        return relations;
    }

    public List<Relation> getIncludedUsesRelations() {
        Set<Relation> relations = new HashSet<>();
        for (AbstractJavaStructure struct : map.values()) {
            if (isStructureIncluded(struct.name)) {
                for (AbstractJavaElement other : struct.subElements) {
                    if (other instanceof JavaMethod) {
                        if (isStructureIncluded(other.type.name)) {
                            relations.add(new Relation(struct, other.type));
                        }

                        for (AbstractJavaStructure arg : ((JavaMethod) other).arguments) {
                            if (isStructureIncluded(arg.name)) {
                                relations.add(new Relation(struct, arg));
                            }
                        }
                    }
                }
            }

        }
        return new ArrayList<>(relations);
    }

    public List<Relation> getIncludedAssociationReltiations() {
        Set<Relation> relations = new HashSet<>();
        for (AbstractJavaStructure struct : map.values()) {
            if (isStructureIncluded(struct.name)) {
                for (AbstractJavaElement other : struct.subElements) {
                    if (isStructureIncluded(other.name)) {
                        if (other instanceof JavaField) {
                            relations.add(new Relation(struct, other.type));
                        }
                    }
                }
            }
        }
        return new ArrayList<>(relations);
    }

    public Collection<AbstractJavaStructure> getStructures() {
        return map.values();
    }

    public void addMethodCallGroup(MethodCallGroup group) {
        this.methodGroups.add(group);
    }

    private void convertMethodCallLinesToStructure() {
        for (MethodCallGroup group : this.methodGroups) {
            AbstractJavaStructure caller = Utils.getInstanceOrJavaStructure(this, group.classCaller);
            for (MethodCallLine line : group.lines) {
                AbstractJavaStructure other = Utils.getInstanceOrJavaStructure(this, line.classOf);

                JavaMethod method = caller.getMethodByQualifiedName(group.method, this);
                JavaMethod otherMethod = other.getMethodByQualifiedName(line.method, this);

                if (otherMethod == null) {
                    boolean isConstuctor = other.name.equals(line.method.methodName);

                    otherMethod = new JavaMethod(other, line.method.methodName, new PublicModifier(), new LinkedList<>(), Utils.getInstanceOrJavaStructure(this, line.returnType), Utils.getInstanceOrJavaStructures(this, Utils.getListOfArgs(line.method.methodDesc).toArray(new String[0])), isConstuctor);
                    other.addSubElement(otherMethod);
                }

                method.addMethodCall(otherMethod);
            }
        }
    }

    public void finalize(List<IPatternCheck> patterns, List<IStructureVisitor> structVisitors) {
        convertMethodCallLinesToStructure();
        checkForPatterns(patterns);

        runStructVisitors(structVisitors);
    }

    private void runStructVisitors(List<IStructureVisitor> structVisitors) {
        for (IStructureVisitor v : structVisitors) {
            HeliosLogger.info("Running Structure Visitor on Class : " + v.getClass().getName());
            this.accept(v);
        }
    }

    private void checkForPatterns(List<IPatternCheck> patterns) {
        for (IPatternCheck check : patterns) {
            HeliosLogger.info("Running Pattern Check on Class : " + check.getClass().getName());
            List<IPattern> list = check.check(this);
            this.patterns.addAll(list);
        }
    }

    public void accept(IUMLVisitor v) throws IOException {
        HeliosLogger.info("Running IUMLVisitor on Class : " + v.getClass().getName());

        v.visitStart();

        for (String name : map.keySet()) {
            if (isStructureIncluded(name))
                map.get(name).accept(v);
        }
        v.visitRelations(this);
        v.visitPatterns(this);

        v.visitEnd();
    }

    public void accept(IStructureVisitor v) {
        for (AbstractJavaStructure s : this.map.values()) {
            List<IPattern> list = v.visit(this, s);
            this.patterns.addAll(list);
        }
    }

    public List<String> getStereotypes(AbstractJavaStructure struct) {
        List<String> steros = new LinkedList<>();

        for (IPattern p : patterns) {
            String s = p.getStereotype(struct);
            if (s != null)
                steros.add(s);
        }

        return steros;
    }

    public List<IPattern> getPatterns() {
        return patterns;
    }

    public boolean isStructureIncluded(String s) {
        return this.includedClasses.contains(s);
    }

}
