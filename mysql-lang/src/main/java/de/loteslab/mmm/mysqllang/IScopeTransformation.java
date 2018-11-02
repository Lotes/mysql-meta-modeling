package de.loteslab.mmm.mysqllang;

public interface IScopeTransformation {
	ScopeAction getAction();
	ISymbol getSymbol();
	/**
	 * applies operations on scope, returns all required symbols
	 * @param scope
	 * @return
	 */
	Iterable<ISymbol> execute(IScope scope);
}
