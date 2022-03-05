package eu.heliosteam.heliosumlgen;

import eu.heliosteam.heliosumlgen.asm.*;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import eu.heliosteam.heliosumlgen.javaModel.checks.PatternFindingFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.Set;

public class JavaModelClassVisitor {

	public final Set<String> classes;

	private final JavaModel model;


	public JavaModelClassVisitor(Set<String> classes) {
		this(classes, null);
	}

	public JavaModelClassVisitor(String classSearch, QualifiedMethod methodSearch, int depth) {
		this(null, classSearch);
	}
	
	public JavaModelClassVisitor(Set<String> classes, String classSearch) {
		this.classes = classes;

		this.model = new JavaModel(classes);

	}

	public void buildUMLModelDefault() {
		buildUMLModelOnly();
		model.finalize(PatternFindingFactory.getPatternChecks(), PatternFindingFactory.getStructureVisitors());
	}
	
	public void buildUMLModelOnly() {
		HeliosLogger.success("Building UML Model ~HeliosTeam");
		for (String className : this.classes) {
			try {
				ClassReader reader = new ClassReader(className); 
				ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, model);
				ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, className, model);
				ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, className, model);
				
				reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
			} catch (IOException e) {
				HeliosLogger.error("Class not found : " + className);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public JavaModel getModel() {
		return this.model;
	}
}
