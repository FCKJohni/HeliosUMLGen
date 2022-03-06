package dev.heliosteam.heliosumlgen.digraph;

import dev.heliosteam.heliosumlgen.HeliosLogger;
import dev.heliosteam.heliosumlgen.HeliosUMLGen;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DigraphBuilder {

    private HeliosUMLGen heliosUMLGen;
    private OutputStream outputStream;
    private boolean shortName;
    private boolean silentType;

    private final String BOILER_PLATE = "digraph G { fontname = \"Bitstream Vera Sans\" fontsize = 8 node [ fontname = \"Bitstream Vera Sans\" fontsize = 8 shape = \"record\" ] edge [ fontname = \"Bitstream Vera Sans\" fontsize = 8 ]\n";

    @SneakyThrows
    public DigraphBuilder(HeliosUMLGen heliosUMLGen, File path, boolean shortName, boolean silentType) {
        this.heliosUMLGen = heliosUMLGen;
        this.shortName = shortName;
        this.silentType = silentType;
        try {
            this.outputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            HeliosLogger.error("Failed opening OutputStream");
            System.exit(-1);
        }

        outputStream.write(BOILER_PLATE.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public void addClass(Class<?> clazz){
        String name = getShortenedName(clazz.getName());
        outputStream.write(String.format("\"%s\"", name).getBytes());

        outputStream.write(String.format(" [ label = \"{%s", name).getBytes());

        outputStream.write("|".getBytes());
    }

    @SneakyThrows
    public void addField(Field field){
        String className = getShortenedName(field.getType().getName());
        outputStream.write(String.format((silentType ? "%s%s\\l" : "%s%s : %s\\l"), getVisibility(field.getModifiers()),  field.getName(), (silentType ? "" : className)).getBytes());
    }

    @SneakyThrows
    public void addMethod(Method method){
        String className = getShortenedName(method.getReturnType().getName());
        outputStream.write(String.format((silentType ? "%s%s(%s)\\l" : "%s%s(%s) : %s\\l"), getVisibility(method.getModifiers()), method.getName().replace("<", "\\<").replace(">", "\\>"),
                Arrays.stream(method.getParameters()).map(Parameter::getName).collect(Collectors.joining(", ")), (silentType ? "" : className)).getBytes());
    }

    private String getVisibility(int modifier) {
        if(Modifier.isPublic(modifier)) return "+ ";
        if(Modifier.isPrivate(modifier)) return "\\- ";
        if(Modifier.isProtected(modifier)) return "# ";
        return "";
    }

    @SneakyThrows
    public void endFields(){
        outputStream.write("|".getBytes());
    }

    @SneakyThrows
    public void endMethods(){
        outputStream.write("}\" ]\n".getBytes());
    }

    @SneakyThrows
    public void end(){
        outputStream.write("}".getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getShortenedName(String input) {
        if(!shortName) return input;
        return input.split("\\.")[input.split("\\.").length - 1];
    }

    public void addRelations() {
    }
}
