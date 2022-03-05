package eu.heliosteam.heliosumlgen.asm;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.JavaField;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import java.util.Objects;

public class ClassFieldVisitor extends ClassVisitor {

	private final String className;
	private final JavaModel model;

	public ClassFieldVisitor(int api, ClassVisitor decorated, String className, JavaModel model) {
		super(api, decorated);
		this.className = className;
		this.model = model;
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();

		AbstractJavaStructure structure = model.getStructure(Utils.getCleanName(this.className));

		if (signature == null) {
			JavaField typeClass = new JavaField(structure, name, Utils.getAccessModifier(access), Utils.getModifiers(access),
					Utils.getInstanceOrJavaStructure(model, type));

			structure.addSubElement(typeClass);
		} else {
			for (String s : Utils.getGenericsPart(signature)) {
				JavaField typeClass;
				typeClass = new JavaField(structure, name, Utils.getAccessModifier(access), Utils.getModifiers(access),
						Utils.getInstanceOrJavaStructure(model, Objects.requireNonNullElseGet(s, () -> Utils.getWithoutGenerics(signature))));
				structure.addSubElement(typeClass);					
				
				
			}
		}
		return toDecorate;
	}
}