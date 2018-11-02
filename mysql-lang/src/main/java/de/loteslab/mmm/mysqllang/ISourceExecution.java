package de.loteslab.mmm.mysqllang;

import de.loteslab.mmm.mysqllang.IExpectationStatement;

public interface ISourceExecution {
	Iterable<IScopeTransformation> getTransformations();
	Iterable<IExpectationStatement> getExpectations();
}
