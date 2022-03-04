package eu.heliosteam.heliosumlgen.asm;

import eu.heliosteam.heliosumlgen.javaModel.JavaModel;
import org.objectweb.asm.MethodVisitor;

public class ClassMethodLineVisitor extends MethodVisitor {

    private final MethodCallGroup method;

    public ClassMethodLineVisitor(int api, MethodVisitor cv, MethodCallGroup method, JavaModel model) {
        super(api, cv);

        this.method = method;

        model.addMethodCallGroup(method);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);

        if (name.equals("<init>")) {
            name = owner;
        }
        name = Utils.shortName(Utils.getCleanName(name));

        method.addLine(new MethodCallLine(Utils.getCleanName(owner), new QualifiedMethod(name, desc), Utils.getReturnType(desc)));
    }
}
