package dev.heliosteam.heliosumlgen;

import dev.heliosteam.heliosumlgen.digraph.DigraphBuilder;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class UMLProcessExecutor {

    private HeliosUMLGen heliosUMLGen;

    public UMLProcessExecutor(HeliosUMLGen heliosUMLGen) {
        this.heliosUMLGen = heliosUMLGen;
    }

    public void beginModelBuild(List<Class<?>> classes, boolean shortName, boolean silentType, File output) {
        DigraphBuilder digraphBuilder = new DigraphBuilder(heliosUMLGen, output, shortName, silentType);
        for (Class<?> aClass : classes) {
            digraphBuilder.addClass(aClass);

            for (Field field : aClass.getDeclaredFields()) {
                digraphBuilder.addField(field);
            }

            digraphBuilder.endFields();

            for (Method method : aClass.getMethods()) {
                if (method.getDeclaringClass().equals(Object.class)) continue;
                digraphBuilder.addMethod(method);
            }

            digraphBuilder.endMethods();
            digraphBuilder.handleRelations(aClass);
        }
        digraphBuilder.end();
    }

    public File generatePNG(File input, File output) {
        try {
            FileInputStream stream = new FileInputStream(input);
            MutableGraph graph = new Parser().read(stream);
            Graphviz.fromGraph(graph).engine(Engine.DOT).width(700).render(Format.PNG).toFile(output);
            HeliosLogger.success("Successfully generated Graphviz Image [" + output.getName() + "]");
        } catch (Exception e) {
            HeliosLogger.error("Failed generating Graphviz Image");
            e.printStackTrace();
        }
        return output;
    }

}
