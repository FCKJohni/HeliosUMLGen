package eu.heliosteam.heliosumlgen.asm;

public class MethodCallLine {

    public final String classOf;
    public final QualifiedMethod method;
    public final String returnType;

    public MethodCallLine(String classOf, QualifiedMethod method, String returnType) {
        this.classOf = classOf;
        this.method = method;
        this.returnType = returnType;
    }
}
