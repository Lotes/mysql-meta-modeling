package de.loteslab.mmm.mysqllang;

public interface ISymbolFactory {
	ISymbolType createSymbolType(String name);
	ISymbol createSymbol(String name, ISymbolType type, ISourceExecution execution);
}
