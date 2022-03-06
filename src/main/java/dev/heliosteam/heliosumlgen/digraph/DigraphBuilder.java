package dev.heliosteam.heliosumlgen.digraph;

import dev.heliosteam.heliosumlgen.HeliosLogger;
import dev.heliosteam.heliosumlgen.HeliosUMLGen;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DigraphBuilder {

    private HeliosUMLGen heliosUMLGen;
    private Writer writer;
    private boolean shortName;
    private boolean silentType;

    private final String BOILER_PLATE = "digraph G { fontname = \"Bitstream Vera Sans\" fontsize = 8 node [ fontname = \"Bitstream Vera Sans\" fontsize = 8 shape = \"record\" ] edge [ fontname = \"Bitstream Vera Sans\" fontsize = 8 ]\n";

    @SneakyThrows
    public DigraphBuilder(HeliosUMLGen heliosUMLGen, File path, boolean shortName, boolean silentType) {
        this.heliosUMLGen = heliosUMLGen;
        this.shortName = shortName;
        this.silentType = silentType;
        try {
            this.writer = new FileWriter(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            HeliosLogger.error("Failed opening OutputStream");
            System.exit(-1);
        }

        writer.write(BOILER_PLATE);
    }

    @SneakyThrows
    public void addClass(Class<?> clazz) {
        String name = getShortenedName(clazz.getName());
        writer.write(String.format("\"%s\"", name));

        writer.write(String.format(" [ label = \"{%s", name));

        writer.write("|");
    }

    @SneakyThrows
    public void addField(Field field) {
        String className = getShortenedName(field.getType().getName());
        writer.write(String.format((silentType ? "%s%s\\l" : "%s%s : %s\\l"), getVisibility(field.getModifiers()), field.getName(), (silentType ? "" : className)));
    }

    @SneakyThrows
    public void addMethod(Method method) {
        String className = getShortenedName(method.getReturnType().getName());
        writer.write(String.format((silentType ? "%s%s(%s)\\l" : "%s%s(%s) : %s\\l"), getVisibility(method.getModifiers()), method.getName().replace("<", "\\<").replace(">", "\\>"),
                Arrays.stream(method.getParameters()).map(Parameter::getName).collect(Collectors.joining(", ")), (silentType ? "" : className)));
    }

    private String getVisibility(int modifier) {
        if (Modifier.isPublic(modifier)) return "+ ";
        if (Modifier.isPrivate(modifier)) return "- ";
        if (Modifier.isProtected(modifier)) return "# ";
        return "";
    }

    @SneakyThrows
    public void handleRelations(Class<?> clazz) {

        //TODO: Fix Relations, allow for direct Association between the Classes instead of adding another Node for the Varname
        for (Pair<String, String> relation : getRelations(clazz)) {
            writer.write(String.format("\"%s\"" + " -> \"%s\" [arrowhead=\"vee\", style=\"filled\"]\n", getShortenedName(relation.getKey()), getShortenedName(relation.getValue())));
        }
    }

    public List<Pair<String, String>> getRelations(Class<?> clazz) {
        List<Pair<String, String>> result = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (!declaredField.getType().equals(clazz) && !declaredField.getType().getTypeName().contains("java")) {
                result.add(Pair.of(declaredField.getName(), declaredField.getType().getTypeName()));
            }
        }
        return result;
    }

    @SneakyThrows
    public void endFields() {
        writer.write("|");
    }

    @SneakyThrows
    public void endMethods() {
        writer.write("}\" ]\n");
    }

    @SneakyThrows
    public void end() {
        writer.write("}");
        writer.flush();
        writer.close();
    }

    private String getShortenedName(String input) {
        if (!shortName) return input;
        return input.split("\\.")[input.split("\\.").length - 1];
    }

}
