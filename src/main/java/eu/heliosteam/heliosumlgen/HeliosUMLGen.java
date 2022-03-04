package eu.heliosteam.heliosumlgen;

import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.UMLDotVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

public class HeliosUMLGen {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Calls shown bellow");
            System.out.println("UML -c <List of Classes> -p <List of Packages>");
            System.out.println("EXAMPLE");
            return;
        }
        OutputStream out;
        switch (args[0]) {
            case "UML" -> {
                Set<String> classesToVisit = new HashSet<>(getClassesFromArgs(args));
                for (String s : getPackagesFromArgs(args)) {
                    classesToVisit.addAll(getClasses(s));
                }
                System.out.println(classesToVisit);
                JavaModelClassVisitor visitor = new JavaModelClassVisitor(classesToVisit);
                visitor.buildUMLModelDefault();
                out = new FileOutputStream("output.txt");
                IUMLVisitor umlVisitor = new UMLDotVisitor(out, visitor.getModel());
                visitor.getModel().accept(umlVisitor);
            }
            case "JSON" -> new JsonHandler(args).run();
            default -> System.out.println("Not a valid diagram type. Valid Types: UML");
        }
    }

    private static List<String> getPackagesFromArgs(String[] args) {
        List<String> toReturn = new ArrayList<>();

        int index = 0;
        while (index < args.length && !args[index++].equals("-p")) {
        }

        for (int i = index; i < args.length; i++) {
            if (args[i].equals("-c")) {
                break;
            }
            toReturn.add(args[i]);
        }
        return toReturn;
    }

    private static List<String> getClassesFromArgs(String[] args) {
        List<String> toReturn = new ArrayList<>();

        int index = 0;
        while (index < args.length && !args[index++].equals("-c")) {
        }

        for (int i = index; i < args.length; i++) {
            if (args[i].equals("-p")) {
                break;
            }

            toReturn.add(args[i]);
        }

        return toReturn;
    }


    /* From
     * http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection?lq=1
     * by user Amit
    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName
     *            The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
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

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
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
