package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolType;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SymbolsVisitor extends MySqlParserBaseVisitor<Iterable<ISymbol>> {
	private ISymbolFactory symbolFactory;
	
	public SymbolsVisitor(ISymbolFactory symbolFactory) {
		this.symbolFactory = symbolFactory;
	}
	
	@Override
	public Iterable<ISymbol> visitTableName(TableNameContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = ctx.name.getText();
		ISymbolType type = symbolFactory.createSymbolType(SymbolNames.TABLE_LIKE);
		ISymbol tableSymbol = symbolFactory.createSymbol(name, type);
		symbols.add(tableSymbol);
		return symbols;
	}
}
