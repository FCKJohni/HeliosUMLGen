package eu.heliosteam.heliosumlgen;

import eu.heliosteam.heliosumlgen.asm.QualifiedMethod;
import eu.heliosteam.heliosumlgen.javaModel.visitor.ISequenceVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.SDSequenceVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.UMLDotVisitor;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

public class HeliosUMLGen {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

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

        Option name = new Option("s", "short", false, "Whether or not to use long Class names");
        options.addOption(name);

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
            Set<String> classesToVisit = new HashSet<String>(List.of(commandLine.getOptionValues("classes")));
            if (commandLine.hasOption("packages")) {
                for (String s : commandLine.getOptionValues("packages")) {
                    classesToVisit.addAll(getClasses(s));
                }
            }

            visitor = new JavaModelClassVisitor(classesToVisit);
            visitor.buildUMLModelDefault();
            out = new FileOutputStream("output.txt");
            IUMLVisitor umlVisitor = new UMLDotVisitor(out, visitor.getModel(), commandLine.hasOption("short"));
            visitor.getModel().accept(umlVisitor);
        } else {
            System.out.println("Not a valid diagram type. Valid Types: UML");
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
