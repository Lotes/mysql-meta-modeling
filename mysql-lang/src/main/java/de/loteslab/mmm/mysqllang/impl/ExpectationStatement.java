package de.loteslab.mmm.mysqllang.impl;

import de.loteslab.mmm.mysqllang.Expectation;
import de.loteslab.mmm.mysqllang.IExpectationStatement;
import de.loteslab.mmm.mysqllang.ISymbol;

public class ExpectationStatement implements IExpectationStatement {
	private Expectation expectation;
	private ISymbol symbol;
	public ExpectationStatement(Expectation expectation, ISymbol symbol) {
		this.expectation = expectation;
		this.symbol = symbol;
	}
	public ISymbol getSymbol() {
		return symbol;
	}
	public Expectation getExpectation() {
		return expectation;
	}
}
