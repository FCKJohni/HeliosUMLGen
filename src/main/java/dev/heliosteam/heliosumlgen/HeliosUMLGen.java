package dev.heliosteam.heliosumlgen;

import dev.heliosteam.heliosumlgen.utils.ClassFinder;
import org.apache.commons.cli.*;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HeliosUMLGen {

    private UMLProcessExecutor umlProcessExecutor;

    public static void main(String[] args) {
        new HeliosUMLGen(args);
    }

    public HeliosUMLGen(String[] args) {
        this.umlProcessExecutor = new UMLProcessExecutor(this);
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

        Option name = new Option("sn", "shortName", false, "Whether or not to use long Class names");
        options.addOption(name);

        Option packageRecursive = new Option("pr", "packageRecursive", false, "Whether or not to search for Classes inside the speicifed Package recursivly");
        options.addOption(packageRecursive);

        Option silent = new Option("sv", "silentVisibility", false, "Whether or not to display Visibility");
        options.addOption(silent);

        Option silentType = new Option("st", "silentType", false, "Whether or not to display Types");
        options.addOption(silentType);

        Option digraphTextOutput = new Option("o", "textOutput", true, "What file to save the raw Digraph in");
        options.addOption(digraphTextOutput);

        Option digraphPNGOutput = new Option("op", "pngOutput", true, "What file to save the Picture of the Digraph in");
        options.addOption(digraphPNGOutput);

        Option generatePNG = new Option("png", "generatePNG", false, "Whether or not to generate the PNG");
        options.addOption(generatePNG);

        Option openPNG = new Option("open", "openPNG", false, "Whether or not to open the PNG after generation");
        options.addOption(openPNG);

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

        String typeString = commandLine.getOptionValue("type");

        if (Objects.equals(typeString, "UML")) {
            List<Class<?>> currentClasses = new ArrayList<>();
            if (commandLine.hasOption("packages")) {
                currentClasses.addAll(ClassFinder.retrieveClassesByPackage(commandLine.getOptionValue("packages"), commandLine.hasOption("packageRecursive")));
            } else if (commandLine.hasOption("classes")) {
                currentClasses.add(ClassFinder.getClass(commandLine.getOptionValue("classes")));
            }

            if (!currentClasses.isEmpty()) {
                HeliosLogger.warn("##############################");
                File text = (commandLine.hasOption("textOutput") ? new File(commandLine.getOptionValue("textOutput")) : new File("output.helios"));
                File png = (commandLine.hasOption("pngOutput") ? new File(commandLine.getOptionValue("pngOutput")) : new File("output.png"));
                HeliosLogger.info("Beginning generation of Digraph Model");
                umlProcessExecutor.beginModelBuild(currentClasses, commandLine.hasOption("shortName"), commandLine.hasOption("silentType"), text);
                HeliosLogger.success("Generation completed");
                HeliosLogger.warn("##############################");
                if (commandLine.hasOption("generatePNG")) {
                    HeliosLogger.success("Beginning generation of Digraph PNG");
                    File output = umlProcessExecutor.generatePNG(text, png);
                    HeliosLogger.success("generation completed");
                    if (commandLine.hasOption("openPNG")) {
                        String[] osIndependentCommand = {"cmd.exe", "/c", "start", "\"Dummy\"", "\"" + output.getAbsolutePath() + "\""};
                        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
                            osIndependentCommand = new String[]{"\"", output.getAbsolutePath() + "\""};
                        }
                        ProcessBuilder processBuilder = new ProcessBuilder(osIndependentCommand);
                        try {
                            processBuilder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                            HeliosLogger.error("Failed to open PNG");
                        }
                    }
                }
            } else {
                HeliosLogger.error("No Classes or Packages Specified!");
                System.exit(-1);
            }
        } else {
            HeliosLogger.error("Invalid Type");
        }
    }


}
