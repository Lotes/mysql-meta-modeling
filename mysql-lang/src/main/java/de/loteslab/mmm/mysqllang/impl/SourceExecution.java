package de.loteslab.mmm.mysqllang.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import de.loteslab.mmm.mysqllang.Expectation;
import de.loteslab.mmm.mysqllang.IExpectationStatement;
import de.loteslab.mmm.mysqllang.IScope;
import de.loteslab.mmm.mysqllang.IScopeTransformation;
import de.loteslab.mmm.mysqllang.ISourceExecution;
import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ScopeAction;

public class SourceExecution implements ISourceExecution {
	public static final ISourceExecution EMPTY = new SourceExecution();
	private LinkedList<IScopeTransformation> transformations = new LinkedList<IScopeTransformation>();
	
	public void addAction(ScopeAction action, ISymbol symbol) {
		transformations.add(ScopeTransformation.create(action, symbol));
	}
	
	@Override
	public Iterable<IScopeTransformation> getTransformations() {
		return transformations;
	}
	
	public void addExecution(ISourceExecution execution) {
		for(IScopeTransformation action: execution.getTransformations())
			transformations.add(action);
	}

	@Override
	public Iterable<IExpectationStatement> getExpectations() {
		HashSet<ISymbol> needed = new HashSet<ISymbol>();
		HashSet<ISymbol> everCreated = new HashSet<ISymbol>();
		
		IScope currentScope = new Scope();
		for(IScopeTransformation transformation: transformations) {
			Iterable<ISymbol> requiredSymbols = transformation.execute(currentScope);
			//collect needed symbols
			for(ISymbol symbol: requiredSymbols)
				needed.add(symbol);
			//get created
			for(ISymbol symbol: currentScope)
				everCreated.add(symbol);
		}
		
		ArrayList<IExpectationStatement> result = new ArrayList<IExpectationStatement>();
		for(ISymbol need: needed) 
			result.add(new ExpectationStatement(Expectation.NEEDED, need));
		for(ISymbol symbol: everCreated)
			if(!currentScope.contains(symbol))
				result.add(new ExpectationStatement(Expectation.DROPPED, symbol));
		for(ISymbol symbol: currentScope)
			result.add(new ExpectationStatement(Expectation.CREATED, symbol));
		return result;
	}
}
