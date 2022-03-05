package eu.heliosteam.heliosumlgen.asm;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.JavaMethod;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class ClassMethodVisitor extends ClassVisitor {

	private final String className;
	private final JavaModel model;

	public ClassMethodVisitor(int api, ClassVisitor decorated, String className, JavaModel model) {
		super(api, decorated);
		this.className = className;
		this.model = model;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		boolean isConstructor = false;
		
		if(name.contains("<init>")) {
			isConstructor = true;
			name = name.replace("<init>", Utils.shortName(className));
		}
		
		if (name.contains("<"))
			return toDecorate;
		
		MethodCallGroup method = new MethodCallGroup(className, new QualifiedMethod(name, desc));
		toDecorate = new ClassMethodLineVisitor(this.api, toDecorate, method, this.model);
		
		
		AbstractJavaStructure structure = model.getStructure(this.className);

		AbstractJavaStructure returnType = Utils.getInstanceOrJavaStructure(model, Utils.getReturnType(desc));
		List<AbstractJavaStructure> arguments = Utils.getInstanceOrJavaStructures(model,
				Utils.getListOfArgs(desc).toArray(new String[0]));

		structure.addSubElement(new JavaMethod(Utils.getInstanceOrJavaStructure(model, className), name, Utils.getAccessModifier(access),
				Utils.getModifiers(access), returnType, arguments, isConstructor ));

		return toDecorate;
	}
}