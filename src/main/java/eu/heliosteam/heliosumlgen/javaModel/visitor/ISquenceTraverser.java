package eu.heliosteam.heliosumlgen.javaModel.visitor;

import java.io.IOException;

public interface ISquenceTraverser {
	void accept(ISequenceVisitor v) throws IOException;
}
