package de.loteslab.mmm.mysqllang;

public interface IScope extends Iterable<ISymbol> {
	IScope copy();
	void add(ISymbol symbol);
	void remove(ISymbol symbol);
	boolean contains(ISymbol symbol);
}
