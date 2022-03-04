package eu.heliosteam.heliosumlgen.javaModel;

import eu.heliosteam.heliosumlgen.javaModel.modifier.IAccessModifier;
import eu.heliosteam.heliosumlgen.javaModel.modifier.IModifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JavaMethod extends AbstractJavaElement {
    public final List<AbstractJavaStructure> arguments;

    public final List<JavaMethod> methodCalls;

    public final boolean isConstructor;

    public JavaMethod(AbstractJavaStructure structure, String name, IAccessModifier access, List<IModifier> modifiers, AbstractJavaStructure type,
                      List<AbstractJavaStructure> arguments, boolean isConstructor) {
        super(structure, name, access, modifiers, type);
        this.arguments = arguments;
        this.isConstructor = isConstructor;
        this.methodCalls = new LinkedList<>();
    }

	public void addMethodCall(JavaMethod method) {
        if (method == null) {
            System.out.println("Null method parm");
            return;
        }
        methodCalls.add(method);
    }

    public String argumentsToString() {
        List<String> args = new ArrayList<>();

        for (AbstractJavaStructure s : arguments)
            args.add(s.name);

        return String.join(", ", args);
    }
}
