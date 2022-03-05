package eu.heliosteam.heliosumlgen.javaModel.visitor;


import eu.heliosteam.heliosumlgen.asm.QualifiedMethod;
import eu.heliosteam.heliosumlgen.javaModel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SDSequenceVisitor implements ISequenceVisitor {

	private final String className;
	private final QualifiedMethod method;
	private final int depth;
	private final OutputStream out;
	
	public SDSequenceVisitor(String className, QualifiedMethod method, int depth, OutputStream out) {
		this.className = className;
		this.method = method;
		this.depth = depth;
		this.out = out;
	}

	@Override
	public void visit(JavaModel model) throws IOException {
		AbstractJavaStructure struct = model.getStructure(className);
		JavaMethod element = struct.getMethodByQualifiedName(this.method, model);
		
		if(element == null ||  !(element instanceof JavaMethod)) {
			return;
		}

		Set<String> objects = new HashSet<>();
		List<String> sdCalls = new ArrayList<>();
		
		
		objects.add(className.replace(".", "\\."));
		
		addCalls(this.depth, objects, sdCalls, (JavaMethod)element);
		
		for(String s: objects) {
			out.write(String.format("%s:%s[a]\n", s,s).getBytes());
		}
		out.write("\n".getBytes());
		
		for(String s: sdCalls) {
			out.write(String.format("%s\n", s).getBytes());
		}
	}
	
	public void addCalls(int depth, Set<String> objects, List<String> sdCalls, JavaMethod method) {
		if(depth == 0)
			return;
		for(JavaMethod call: method.methodCalls) {
			String str = call.owner.name.replace(".", "\\.");
			if(method.owner.name.equals(call.owner.name) && (depth == 1 || call.methodCalls.size() == 0)) {
				sdCalls.add(String.format("%s:.%s(%s)", method.owner.name.replace(".", "\\."), call.name, call.argumentsToString()));
			} else {
				sdCalls.add(String.format("%s:%s=%s.%s(%s)", method.owner.name.replace(".", "\\."), call.type.name ,call.owner.name.replace(".", "\\."), call.name, call.argumentsToString().replace(".", "\\.")));					
			}
			objects.add(str);
			addCalls(depth - 1, objects, sdCalls, call);
		}
	}
	
	
}