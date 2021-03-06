package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.javaModel.visitor.IStructureVisitor;
import eu.heliosteam.heliosumlgen.javaModel.visitor.SingletonVisitor;

import java.util.*;

public class PatternFindingFactory {

	static final Map<String, Class<? extends IPatternCheck>> patterns;
	static final Map<String, Class<? extends IStructureVisitor>> visitors;
	
	static {
		patterns = new HashMap<>();
		visitors = new HashMap<>();
		
		patterns.put("Decorator-Detection", DecoratorCheck.class);
		patterns.put("Adapter-Detection", AdapterCheck.class);
		patterns.put("Composite-Detection", CompositeCheck.class);
		
		visitors.put("Singleton-Detection", SingletonVisitor.class);
	}
	
	public static List<IPatternCheck> getPatternChecks() {
		List<IPatternCheck> toReturn = new LinkedList<>();

		toReturn.add(new AdapterCheck());
		toReturn.add(new DecoratorCheck());
		toReturn.add(new CompositeCheck());
		
		return toReturn;
	}

	public static List<IStructureVisitor> getStructureVisitors() {
		List<IStructureVisitor> toReturn = new LinkedList<>();
		
		toReturn.add(new SingletonVisitor());
		
		return toReturn;
	}

}
