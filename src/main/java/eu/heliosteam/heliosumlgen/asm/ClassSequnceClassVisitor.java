package eu.heliosteam.heliosumlgen.asm;

import eu.heliosteam.heliosumlgen.javaModel.AbstractJavaStructure;
import eu.heliosteam.heliosumlgen.javaModel.JavaMethod;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassSequnceClassVisitor extends ClassDeclarationVisitor {

	private final JavaModel model;
	private Set<QualifiedMethod> methodsToFind;
	private final int depth;
	private final SequenceStructure seqStructure;
	private final String className;
	
	public ClassSequnceClassVisitor(int api, JavaModel model, String className, Set<QualifiedMethod> methodsToFind, int depth, SequenceStructure seqStructure) {
		super(api, model);
		
		this.model = model;
		this.methodsToFind = methodsToFind;
		this.depth = depth;
		this.seqStructure = seqStructure;
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

		boolean isConstructor = false;
		if(name.contains("<init>")) {
			isConstructor = true;
			name = name.replace("<init>", Utils.shortName(className));
		}
		
		QualifiedMethod qmeth = new QualifiedMethod(name, desc);
		if(methodsToFind.contains(qmeth)) {
			MethodCallGroup method = new MethodCallGroup(className, qmeth);
			toDecorate = new ClassSequnceMethodVisitor(this.api, toDecorate, method, this.model, this.depth, seqStructure);
			
			
			AbstractJavaStructure structure = model.getStructure(Utils.getCleanName(this.className));

			AbstractJavaStructure returnType = Utils.getInstanceOrJavaStructure(model, Utils.getReturnType(desc));
			List<AbstractJavaStructure> arguments = Utils.getInstanceOrJavaStructures(model,
					Utils.getListOfArgs(desc).toArray(new String[0]));

			structure.addSubElement(new JavaMethod(Utils.getInstanceOrJavaStructure(model, className), name, Utils.getAccessModifier(access),
					Utils.getModifiers(access), returnType, arguments, isConstructor ));
			
		}
		
		return toDecorate;
	}
	
	@Override
	public void visitEnd() {
		super.visitEnd();
		
		if(depth > 0)
			try {
				Map<String, Set<QualifiedMethod>> methods = seqStructure.getClassMethods();
				seqStructure.vistedAll();
				for(String s: methods.keySet()) {
					ClassReader reader = new ClassReader(s);
					ClassVisitor decVisitor = new ClassSequnceClassVisitor(this.api, model, s, methods.get(s), this.depth - 1, seqStructure);
					reader.accept(decVisitor, ClassReader.EXPAND_FRAMES);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
