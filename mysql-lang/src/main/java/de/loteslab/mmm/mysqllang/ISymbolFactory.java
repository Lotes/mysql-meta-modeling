package de.loteslab.mmm.mysqllang;

public interface ISymbolFactory {
	ISymbolType createSymbolType(String name, ISymbolType superType);
	ISymbol createSymbol(String name, ISymbolType type);
}
