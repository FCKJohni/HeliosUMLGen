package eu.heliosteam.heliosumlgen.javaModel.visitor;

import eu.heliosteam.heliosumlgen.javaModel.*;

import java.io.IOException;

public interface IUMLVisitor {
    void visitStart() throws IOException;

    void visit(JavaClass clazz) throws IOException;

    void visit(JavaInterface clazz) throws IOException;

    void visit(JavaField clazz) throws IOException;

    void visitEndFields() throws IOException;

    void visit(JavaMethod clazz) throws IOException;

    void visitEndStructure() throws IOException;

    void visitRelations(JavaModel model) throws IOException;

    void visitEnd() throws IOException;

    void visitPatterns(JavaModel javaModel) throws IOException;
}
