package eu.heliosteam.heliosumlgen.javaModel.visitor;

import eu.heliosteam.heliosumlgen.JsonConfig;
import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPattern;

import java.util.List;

public interface IStructureVisitor {

    List<IPattern> visit(JavaModel model, AbstractJavaStructure s);

    void setSettings(JsonConfig config);

}
