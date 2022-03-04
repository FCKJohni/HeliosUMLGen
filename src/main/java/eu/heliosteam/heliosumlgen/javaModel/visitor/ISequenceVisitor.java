package eu.heliosteam.heliosumlgen.javaModel.visitor;

import eu.heliosteam.heliosumlgen.javaModel.JavaModel;

import java.io.IOException;

public interface ISequenceVisitor {
    void visit(JavaModel model) throws IOException;
}
