package eu.heliosteam.heliosumlgen.asm;

import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import org.objectweb.asm.MethodVisitor;

public class ClassSequnceMethodVisitor extends ClassMethodLineVisitor {

	private final int depth;
	private final SequenceStructure seqStructure;
	
	public ClassSequnceMethodVisitor(int api, MethodVisitor mv, MethodCallGroup method, JavaModel model, int depth, SequenceStructure seqStructure) {
		super(api, mv, method, model);

		this.depth = depth;
		this.seqStructure = seqStructure;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		if(this.depth > 0) {
			if(name.equals("<init>")) {
				name = owner;
			}
			name = Utils.shortName(Utils.getCleanName(name));
			seqStructure.addMethod(Utils.getCleanName(owner), new QualifiedMethod(name, desc));
		}
	}
}
