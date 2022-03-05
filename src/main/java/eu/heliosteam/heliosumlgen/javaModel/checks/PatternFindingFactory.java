package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.HeliosLogger;
import eu.heliosteam.heliosumlgen.JsonConfig;
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

	public static List<IPatternCheck> getPatternChecks(JsonConfig config) {
		List<IPatternCheck> toReturn = new LinkedList<>();

		List<String> phases = Arrays.asList(config.Phases);

		for(String s: patterns.keySet()) {
			if(phases.contains(s)) {
				try {
					toReturn.add(patterns.get(s).newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					HeliosLogger.error("Invalid IPatternCheck");
					e.printStackTrace();
					System.exit(0);
				}
			}
		}

		return toReturn;
	}

	public static List<IStructureVisitor> getStructureVisitors() {
		List<IStructureVisitor> toReturn = new LinkedList<>();
		
		toReturn.add(new SingletonVisitor());
		
		return toReturn;
	}

	public static List<IStructureVisitor> getStructureVisitors(JsonConfig config) {
		List<IStructureVisitor> toReturn = new LinkedList<>();
		List<String> phases = Arrays.asList(config.Phases);
		for(String s: visitors.keySet()) {
			if(phases.contains(s)) {
				try {
					IStructureVisitor vis = visitors.get(s).newInstance();
					vis.setSettings(config);
					toReturn.add(vis);
				} catch (InstantiationException | IllegalAccessException e) {
					HeliosLogger.error("Invalid IPatternCheck");
					e.printStackTrace();
					System.exit(0);
				}
			}
		}

		return toReturn;
	}
}
