package eu.heliosteam.heliosumlgen;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLogger;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.UMLDotVisitor;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

public class HeliosUMLGen {

    public static void main(String[] args) throws IOException {

        Options options = new Options();

        Option type = new Option("t", "type", true, "Type to use [UML/SEQ]");
        type.setRequired(true);
        options.addOption(type);

        Option classes = new Option("c", "classes", true, "List of Classes");
        classes.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(classes);

        Option packages = new Option("p", "packages", true, "List of packages");
        packages.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(packages);

        Option name = new Option("sn", "short", false, "Whether or not to use long Class names");
        options.addOption(name);

        Option silent = new Option("sv", "silentVisibility", false, "Whether or not to display Visibility");
        options.addOption(silent);

        Option silentType = new Option("st", "silentType", false, "Whether or not to display Types");
        options.addOption(silentType);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine commandLine = null;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("", options);

            System.exit(1);
        }
        OutputStream out;
        String typeString = commandLine.getOptionValue("type");
        JavaModelClassVisitor visitor;
        if (typeString.equals("UML")) {
            Set<String> classesToVisit = new HashSet<>(List.of(commandLine.getOptionValues("classes")));
            if (commandLine.hasOption("packages")) {
                for (String s : commandLine.getOptionValues("packages")) {
                    classesToVisit.addAll(getClasses(s));
                }
            }

            visitor = new JavaModelClassVisitor(classesToVisit);
            visitor.buildUMLModelDefault();
            out = new FileOutputStream("output.txt");
            IUMLVisitor umlVisitor = new UMLDotVisitor(out, visitor.getModel(), commandLine.hasOption("short"), commandLine.hasOption("silentVisibility"), commandLine.hasOption("silentType"));
            visitor.getModel().accept(umlVisitor);
            HeliosLogger.success("UML Model has been generated to output.txt");
            HeliosLogger.warn("Attempting to generate Graphviz Image");
            try {
                FileInputStream stream = new FileInputStream("output.txt");
                Logger logger = (Logger) LoggerFactory.getLogger(Graphviz.class);
                logger.setLevel(Level.OFF);
                System.setProperty("engine.WarnInterpreterOnly", "false");
                MutableGraph graph = new Parser().read(stream);
                Graphviz.fromGraph(graph).width(700).render(Format.PNG).toFile(new File("output.png"));
                HeliosLogger.success("Successfully generated Graphviz Image [output.png]");
            }catch (Exception e){
                HeliosLogger.error("Failed generating Graphviz Image");
                e.printStackTrace();
            }

        } else {
            HeliosLogger.error("Not a valid diagram Type. Valid Types are: UML");
        }
    }


    private static List<String> getClasses(String packageName) throws IOException {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<String> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<String> findClasses(File directory, String packageName) {
        List<String> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        return classes;
    }

}
