package eu.heliosteam.heliosumlgen.asm;

import java.util.LinkedList;
import java.util.List;

public class MethodCallGroup {

    public final String classCaller;
    public final QualifiedMethod method;
    public final List<MethodCallLine> lines;

    public MethodCallGroup(String classCaller, QualifiedMethod method) {
        this.classCaller = classCaller;
        this.method = method;
        lines = new LinkedList<>();
    }

    public void addLine(MethodCallLine line) {
        lines.add(line);
    }
}
