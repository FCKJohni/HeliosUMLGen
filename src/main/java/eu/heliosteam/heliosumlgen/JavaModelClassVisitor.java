package eu.heliosteam.heliosumlgen;

import eu.heliosteam.heliosumlgen.asm.*;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import eu.heliosteam.heliosumlgen.javaModel.checks.IPatternCheck;
import eu.heliosteam.heliosumlgen.javaModel.checks.PatternFindingFactory;
import eu.heliosteam.heliosumlgen.javaModel.visitor.IStructureVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class JavaModelClassVisitor {

	public Set<String> classes;

	private JavaModel model;
	private String classSearch;
	private QualifiedMethod methodSearch;
	private int depth;
	

	public JavaModelClassVisitor(Set<String> classes) {
		this(classes, null, null, 0);
	}

	public JavaModelClassVisitor(String classSearch, QualifiedMethod methodSearch, int depth) {
		this(null, classSearch, methodSearch, depth);
	}
	
	public JavaModelClassVisitor(Set<String> classes, String classSearch, QualifiedMethod methodSearch, int depth) {
		this.classes = classes;

		this.model = new JavaModel(classes);

		this.classSearch = classSearch;
		this.methodSearch = methodSearch;
		this.depth = depth;
	}

	public void buildUMLModelDefault() throws IOException{
		buildUMLModelOnly();
		model.finalize(PatternFindingFactory.getPatternChecks(), PatternFindingFactory.getStructureVisitors());
	}
	
	public void buildUMLModelOnly() {
		System.out.println("Building UML Model");
		for (String className : this.classes) {
			try {
				ClassReader reader = new ClassReader(className); 
				ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, model);
				ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, className, model);
				ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, className, model);
				
				reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
			} catch (IOException e) {
				System.out.println("Class not Found: " + className);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public JavaModel getModel() {
		return this.model;
	}
}
