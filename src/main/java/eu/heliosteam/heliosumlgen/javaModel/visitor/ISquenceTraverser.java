package eu.heliosteam.heliosumlgen.javaModel.visitor;

import java.io.IOException;

public interface ISquenceTraverser {
	public void accept(ISequenceVisitor v) throws IOException;
}
