package eu.heliosteam.heliosumlgen.javaModel.visitor;

import java.io.IOException;

public interface IUMLTraverser {

	void accept(IUMLVisitor v) throws IOException;
}
