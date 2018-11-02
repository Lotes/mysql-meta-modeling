package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;

import de.loteslab.mmm.mysqllang.IScope;
import de.loteslab.mmm.mysqllang.IScopeTransformation;
import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ScopeAction;

public class ScopeTransformation implements IScopeTransformation {
	public static IScopeTransformation create(ScopeAction action, ISymbol symbol) {
		return new ScopeTransformation(action, symbol);
	}

	private ISymbol symbol;
	private ScopeAction action;

	private ScopeTransformation(ScopeAction action, ISymbol symbol) {
		this.action = action;
		this.symbol = symbol;
	}
	
	@Override
	public ScopeAction getAction() {
		return action;
	}

	@Override
	public ISymbol getSymbol() {
		return symbol;
	}

	@Override
	public Iterable<ISymbol> execute(IScope scope) {
		LinkedList<ISymbol> needed = new LinkedList<ISymbol>();
		switch(action) {
		case CREATE:
			scope.add(symbol); 
			break;
		case DELETE:
			scope.remove(symbol);
		case REQUIRE:
			needed.add(symbol);
			break;
		case EXECUTE:
			for(IScopeTransformation transformation: symbol.getExecution().getTransformations())
				transformation.execute(scope);
			break;
		}
		return needed;
	}
}
