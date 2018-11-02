package de.loteslab.mmm.mysqllang.impl;

import java.util.HashSet;
import java.util.Iterator;

import de.loteslab.mmm.mysqllang.IScope;
import de.loteslab.mmm.mysqllang.ISymbol;

public class Scope implements IScope {
	private HashSet<ISymbol> symbols = new HashSet<ISymbol>(); 
	
	@Override
	public void add(ISymbol symbol) {
		symbols.add(symbol);
	}

	@Override
	public void remove(ISymbol symbol) {
		symbols.remove(symbol);
	}

	@Override
	public boolean contains(ISymbol symbol) {
		return symbols.contains(symbol);
	}

	@Override
	public IScope copy() {
		Scope scope = new Scope();
		scope.symbols.addAll(symbols);
		return scope;
	}

	@Override
	public Iterator<ISymbol> iterator() {
		return symbols.iterator();
	}
}
