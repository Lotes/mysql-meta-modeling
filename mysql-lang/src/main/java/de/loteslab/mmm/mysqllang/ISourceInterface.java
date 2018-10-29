package de.loteslab.mmm.mysqllang;

public interface ISourceInterface {
	Iterable<ISymbol> getImports();
	Iterable<ISymbol> getExports();
}
