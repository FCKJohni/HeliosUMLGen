package eu.heliosteam.heliosumlgen.javaModel.visitor;

import eu.heliosteam.heliosumlgen.javaModel.*;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;
import eu.heliosteam.heliosumlgen.javaModel.modifier.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class UMLDotVisitor implements IUMLVisitor {

    private final OutputStream out;
    private final JavaModel model;
    private final boolean shortName;

    public static final String BOILER_PLATE = "digraph G { fontname = \"Bitstream Vera Sans\" fontsize = 8 node [ fontname = \"Bitstream Vera Sans\" fontsize = 8 shape = \"record\" ] edge [ fontname = \"Bitstream Vera Sans\" fontsize = 8 ]\n";
    private final boolean silent;
    private final boolean silentType;


    public UMLDotVisitor(OutputStream out, JavaModel model, boolean shortName, boolean silent, boolean silentType) {
        this.out = out;
        this.model = model;
        this.shortName = shortName;
        this.silent = silent;
        this.silentType = silentType;
    }

    @Override
    public void visitStart() throws IOException {
        out.write(BOILER_PLATE.getBytes());
    }

    @Override
    public void visit(JavaClass clazz) throws IOException {
        String name = getShortenedName(clazz.name);
        out.write(String.format("\"%s\"", name).getBytes());

        out.write(String.format(" [ label = \"{%s", name).getBytes());

        for (String s : model.getStereotypes(clazz)) {
            out.write(String.format("\\l\\<\\<%s\\>\\>", s).getBytes());
        }
        out.write("|".getBytes());
    }

    @Override
    public void visit(JavaInterface clazz) throws IOException {
        String name = getShortenedName(clazz.name);
        out.write(String.format("\"%s\"", name).getBytes());

        out.write(String.format(" [ label = \"{\\<\\<interface\\>\\>\\l%s", name).getBytes());

        for (String s : model.getStereotypes(clazz)) {
            out.write(String.format("\\l\\<\\<%s\\>\\>", s).getBytes());
        }
        out.write("|".getBytes());
    }

    @Override
    public void visit(JavaField clazz) throws IOException {
        String className = getShortenedName(clazz.type.name);
        out.write(String.format((silentType ? "%s%s\\l" : "%s%s : %s\\l"), getAccessModifierString(clazz.access), clazz.name, (silentType ? "" : className))
                .getBytes());
    }

    @Override
    public void visitEndFields() throws IOException {
        out.write("|".getBytes());
    }

    @Override
    public void visit(JavaMethod clazz) throws IOException {
        String className = getShortenedName(clazz.type.name);
        out.write(String.format((silentType ? "%s%s(%s)\\l" : "%s%s(%s) : %s\\l"), getAccessModifierString(clazz.access), clazz.name.replace("<", "\\<").replace(">", "\\>"),
                getArgumentString(clazz.arguments), (silentType ? "" : className)).getBytes());
    }

    @Override
    public void visitEndStructure() throws IOException {
        out.write("}\" ]\n".getBytes());
    }

    @Override
    public void visitEnd() throws IOException {
        out.write("}".getBytes());
    }

    private String getAccessModifierString(IAccessModifier modifier) {
        if (silent) {
            return "";
        }
        if (modifier instanceof PrivateModifier) {
            return "- ";
        } else if (modifier instanceof ProtectedPrivateModifier) {
            return "";
        } else if (modifier instanceof ProtectedModifier) {
            return "# ";
        } else if (modifier instanceof PublicModifier) {
            return "+ ";
        }

        return "";
    }

    private String getArgumentString(List<AbstractJavaStructure> args) {
        List<String> names = new ArrayList<>();
        for (AbstractJavaStructure struct : args)
            names.add(struct.name);

        return String.join(", ", names);
    }

    @Override
    public void visitRelations(JavaModel model) throws IOException {


        // Child Parent
        for (Relation relation : model.getChildParrentIncludedRelations()) {
            out.write(String.format("\"%s\"" + " -> \"%s\" [arrowhead=\"onormal\", style=\"filled\"]\n",
                    getShortenedName(relation.base.name), getShortenedName(relation.other.name)).getBytes());
        }

        // Implements
        for (Relation relation : model.getIncludedInterfaceRelations()) {
            out.write(String.format("\"%s\"" + " -> \"%s\" [arrowhead=\"onormal\", style=\"dashed\"]\n",
                    getShortenedName(relation.base.name), getShortenedName(relation.other.name)).getBytes());
        }

        // Uses
        for (Relation relation : model.getIncludedUsesRelations()) {
            out.write(String.format("\"%s\"" + " -> \"%s\" [arrowhead=\"vee\", style=\"dashed\"]\n", getShortenedName(relation.base.name),
                    getShortenedName(relation.other.name)).getBytes());
        }

        // Association
        for (Relation relation : model.getIncludedAssociationReltiations()) {
            out.write(String.format("\"%s\"" + " -> \"%s\" [arrowhead=\"vee\", style=\"filled\"]\n", getShortenedName(relation.base.name),
                    getShortenedName(relation.other.name)).getBytes());
        }
    }

    @Override
    public void visitPatterns(JavaModel javaModel) throws IOException {
        for (IPattern pattern : javaModel.getPatterns()) {
            for (AbstractJavaStructure struct : pattern.getInvolvedStructes()) {
                if (javaModel.isStructureIncluded(struct.name)) {
                    Color c = pattern.getDefaultColor();
                    out.write(String.format("\"%s\" [style=filled color=black fillcolor=\"#%02x%02x%02x\"]\n", struct.name, c.getRed(), c.getGreen(), c.getBlue()).getBytes());
                }
            }

            for (Relation r : pattern.getTopLevelRelations()) {
                if (javaModel.isStructureIncluded(r.base.name) && javaModel.isStructureIncluded(r.other.name)) {
                    out.write(String.format("\"%s\" -> \"%s\" [label = \"\\<\\<%s\\>\\>\"]\n", r.base.name, r.other.name, pattern.getRelationName()).getBytes());
                }
            }
        }
    }

    private String getShortenedName(String input) {
        if(!shortName) return input;
        return input.split("\\.")[input.split("\\.").length - 1];
    }
}
