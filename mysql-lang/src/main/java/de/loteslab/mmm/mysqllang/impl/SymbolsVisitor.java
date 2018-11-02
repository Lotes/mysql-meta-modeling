package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolType;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CurrentUserContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.OwnerStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TablesContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UserIdContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UserNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SymbolsVisitor extends MySqlParserBaseVisitor<Iterable<ISymbol>> {
	private SymbolFactory symbolFactory;
	private TextVisitor textVisitor;
	
	public SymbolsVisitor(SymbolFactory symbolFactory, TextVisitor textVisitor) {
		this.symbolFactory = symbolFactory;
		this.textVisitor = textVisitor;
	}
	
	@Override
	public Iterable<ISymbol> visitOwnerStatement(OwnerStatementContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx.id);
		ISymbolType type = symbolFactory.Predefined.User;
		ISymbol tableSymbol = symbolFactory.createSymbol(name, type, SourceExecution.EMPTY);
		symbols.add(tableSymbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitTableName(TableNameContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = symbolFactory.Predefined.TableLike;
		ISymbol tableSymbol = symbolFactory.createSymbol(name, type, SourceExecution.EMPTY);
		symbols.add(tableSymbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitUserName(UserNameContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = textVisitor.visit(ctx);
		ISymbolType type = symbolFactory.Predefined.User;
		ISymbol symbol = symbolFactory.createSymbol(name, type, SourceExecution.EMPTY);
		symbols.add(symbol);
		return symbols;
	}
	
	@Override
	public Iterable<ISymbol> visitCurrentUser(CurrentUserContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		String name = "<CURRENT_USER>";
		ISymbolType type = symbolFactory.Predefined.User;
		ISymbol symbol = symbolFactory.createSymbol(name, type, SourceExecution.EMPTY);
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
	
	@Override
	public Iterable<ISymbol> visitTables(TablesContext ctx) {
		LinkedList<ISymbol> symbols = new LinkedList<ISymbol>();
		for(TableNameContext tbl: ctx.tbls)
			for(ISymbol table: visit(tbl))
				symbols.add(table);
		return symbols;
	}
}
