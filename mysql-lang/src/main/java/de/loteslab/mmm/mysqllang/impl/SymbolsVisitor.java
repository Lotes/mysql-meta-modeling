package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolType;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CurrentUserContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LikeTableWithoutParenthesesContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.OwnerStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UserIdContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UserNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SymbolsVisitor extends MySqlParserBaseVisitor<Iterable<ISymbol>> {
	private ISymbolFactory symbolFactory;
	private TextVisitor textVisitor;
	
	public SymbolsVisitor(ISymbolFactory symbolFactory, TextVisitor textVisitor) {
		this.symbolFactory = symbolFactory;
		this.textVisitor = textVisitor;
	}
	
	@Override
	public Iterable<ISymbol> visitOwnerStatement(OwnerStatementContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx.id);
		ISymbolType type = symbolFactory.createSymbolType(SymbolFactory.TypeNames.USER);
		ISymbol tableSymbol = symbolFactory.createSymbol(name, type);
		symbols.add(tableSymbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitTableName(TableNameContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = symbolFactory.createSymbolType(SymbolFactory.TypeNames.TABLE_LIKE);
		ISymbol tableSymbol = symbolFactory.createSymbol(name, type);
		symbols.add(tableSymbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitUserName(UserNameContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx);
		ISymbolType type = symbolFactory.createSymbolType(SymbolFactory.TypeNames.USER);
		ISymbol symbol = symbolFactory.createSymbol(name, type);
		symbols.add(symbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitCurrentUser(CurrentUserContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = "<CURRENT_USER>";
		ISymbolType type = symbolFactory.createSymbolType(SymbolFactory.TypeNames.USER);
		ISymbol symbol = symbolFactory.createSymbol(name, type);
		symbols.add(symbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitUserId(UserIdContext ctx) {
		return takeFirst(ctx);
	}
	
	private Iterable<ISymbol> takeFirst(ParserRuleContext ctx) {
		return visit(ctx.children.get(0));
	}
}
