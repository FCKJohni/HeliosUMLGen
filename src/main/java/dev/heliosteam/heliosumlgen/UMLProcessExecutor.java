package dev.heliosteam.heliosumlgen;

import dev.heliosteam.heliosumlgen.digraph.DigraphBuilder;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class UMLProcessExecutor {

    private final HeliosUMLGen heliosUMLGen;
    private File tempOutput = new File("temp-output.svg");

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
            Graphviz.fromGraph(fetchGraph(input)).engine(Engine.DOT).width(700).render(Format.SVG).toFile(tempOutput);
            fixTransparency(tempOutput);
            transcode(tempOutput, output);
            Files.delete(tempOutput.toPath());
            HeliosLogger.success("Successfully generated Graphviz Image [" + output.getName() + "]");
        } catch (Exception e) {
            HeliosLogger.error("Failed generating Graphviz Image");
            e.printStackTrace();
        }
        return output;
    }

    public MutableGraph fetchGraph(File input) throws IOException {
        FileInputStream stream = new FileInputStream(input);
        return new Parser().read(stream);
    }

    private void fixTransparency(File tempOutput) throws IOException {
        String content = Files.readString(tempOutput.toPath());
        content = content.replace("stroke=\"transparent\"", "stroke=\"white\"");
        Files.writeString(tempOutput.toPath(), content);
    }

    public void transcode(File tempOutput, File output) {
        try {
            TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(tempOutput));
            FileOutputStream fileOutputStream = new FileOutputStream(output);
            PNGTranscoder transcoder = new PNGTranscoder();
            TranscoderOutput transcoderOutput = new TranscoderOutput(fileOutputStream);
            transcoder.transcode(transcoderInput, transcoderOutput);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (TranscoderException | IOException e) {
            HeliosLogger.error("Failed Transcoding SVG to PNG");
        }

    }

}
