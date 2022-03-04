package eu.heliosteam.heliosumlgen;

import com.google.gson.Gson;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import eu.heliosteam.heliosumlgen.javaModel.checks.PatternFindingFactory;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IUMLVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.UMLDotVisitor;

import java.io.*;
import java.util.*;

public class JsonHandler {

    private JsonConfig jsonConfig;

    public JsonHandler(String[] arguments) {
        Gson gson = new Gson();
        FileReader reader;
        jsonConfig = null;
        try {
            reader = new FileReader(arguments[1]);
            jsonConfig = gson.fromJson(reader, JsonConfig.class);
        } catch (FileNotFoundException e) {
            System.out.println("Json File not found");
            System.exit(0);
        }

    }

    public void run() {
        run(jsonConfig);

    }

    public void run(JsonConfig config) {
        Map<String, InputStream> extra = loadClassesInFolder(config.InputFolder);
        Set<String> classesToVisit = getClassList(config);

        File file = new File(config.OutputDirectory);
        String folder = null;
        try {
            folder = file.getCanonicalPath();
            if (!folder.endsWith(File.separator)) {
                folder += File.separator;
            }
            folder += "umlOutput.txt";
        } catch (IOException e1) {
            System.out.println("Unable to find output folder.");
            e1.printStackTrace();
            System.exit(0);
        }

        FileOutputStream stream;
        try {
            List<String> phaseList = Arrays.asList(config.Phases);

            stream = new FileOutputStream(folder);

            Set<String> toReAdd = new HashSet<>();

            for (String s : extra.keySet()) {
                classesToVisit.remove(s);
                toReAdd.add(s);
            }

            JavaModelClassVisitor visitor = new JavaModelClassVisitor(classesToVisit);

            visitor.buildUMLModelOnly();


            for (String s : extra.keySet()) {
                visitor.extendUMLModelFile(s, extra.get(s));
            }

            for (String s : toReAdd) {
                visitor.getModel().addToIncluded(s);
            }

            visitor.runPatternDetection(PatternFindingFactory.getPatternChecks(config), PatternFindingFactory.getStructureVisitors(config));

            JavaModel model = visitor.getModel();

            model.setExclusionList(config.exclusion);

            if (phaseList.contains("DOT-Generation")) {
                IUMLVisitor umlVisitor = new UMLDotVisitor(stream, model);
                model.accept(umlVisitor);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to create file at output directory");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("IO Exception durring writing");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Map<String, InputStream> loadClassesInFolder(String inputFolder) {
        Map<String, InputStream> map = new HashMap<>();

        File folder = new File(inputFolder);

        loadFilesRecur(map, "", folder.listFiles());

        return map;
    }

    private void loadFilesRecur(Map<String, InputStream> map, String path, File[] files) {
        for (File name : files) {
            if (name.isDirectory()) {
                loadFilesRecur(map, path + name.getName() + ".", name.listFiles());
            } else {
                if (name.getName().endsWith(".class")) {
                    try {
                        String className = path + name.getName().replace(".class", "");
                        map.put(className, new FileInputStream(name));
                    } catch (FileNotFoundException e) {
                        System.out.println("File was deleted in middle of reading");
                        System.exit(0);
                    }
                }
            }
        }
    }

    public Set<String> getClassList(JsonConfig config) {

        Set<String> classesToVisit = new HashSet<>(Arrays.asList(config.InputClasses));

        return classesToVisit;
    }
}
