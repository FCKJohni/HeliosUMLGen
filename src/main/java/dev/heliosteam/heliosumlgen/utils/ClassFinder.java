package dev.heliosteam.heliosumlgen.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import dev.heliosteam.heliosumlgen.HeliosUMLGen;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ClassFinder {

    public static List<Class<?>> retrieveClassesByPackage(String packageName, boolean recursive) {
        ImmutableSet<ClassPath.ClassInfo> classes;
        List<Class<?>> result = new ArrayList<>();
        try {
            if (!recursive) {
                classes = ClassPath.from(HeliosUMLGen.class.getClassLoader()).getTopLevelClasses(packageName);
            } else {
                classes = ClassPath.from(HeliosUMLGen.class.getClassLoader()).getTopLevelClassesRecursive(packageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        for (ClassPath.ClassInfo aClass : classes) {
            Class<?> clazz = aClass.load();
            result.add(clazz);
        }

        return result;
    }

    @Nullable
    public static Class<?> getClass(String className){
        try {
            return Class.forName(className, false, HeliosUMLGen.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
