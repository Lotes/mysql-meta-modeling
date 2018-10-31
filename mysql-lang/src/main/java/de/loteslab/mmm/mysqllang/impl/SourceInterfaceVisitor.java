package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;
import java.util.List;

import de.loteslab.mmm.mysqllang.ISourceInterface;
import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateIndexContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DdlStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LastStatementEmptyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LastStatementSqlContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementEmptyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementSqlContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.SqlStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.SqlStatementsContext;

public class SourceInterfaceVisitor extends MySqlParserBaseVisitor<SourceInterface> {
	private ISymbolFactory factory;
	private SymbolsVisitor symbolVisitor;
	
	public SourceInterfaceVisitor(ISymbolFactory factory) {
		this.factory = factory;
		symbolVisitor = new SymbolsVisitor(factory);
	}
	
	@Override
	public SourceInterface visitRoot(RootContext ctx) {
		return visit(ctx.stmts);
	}
	
	@Override
	public SourceInterface visitSqlStatements(SqlStatementsContext ctx) {
		List<SourceInterface> interfaces = new LinkedList<SourceInterface>();
		for(NonLastStatementContext stmt: ctx.nonLasts) {
			SourceInterface iface = this.visit(stmt);
			interfaces.add(iface);
		}
		SourceInterface iface = this.visit(ctx.last);
		interfaces.add(iface);
		return SourceInterface.combine(interfaces);
	}
	
	@Override
	public SourceInterface visitLastStatementEmpty(LastStatementEmptyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitNonLastStatementEmpty(NonLastStatementEmptyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitLastStatementSql(LastStatementSqlContext ctx) {
		return visit(ctx.stmt);
	}
	
	@Override
	public SourceInterface visitNonLastStatementSql(NonLastStatementSqlContext ctx) {
		return visit(ctx.stmt);
	}
	
	@Override
	public SourceInterface visitSqlStatement(SqlStatementContext ctx) {
		return visit(ctx.children.get(0));
	}
	
	@Override
	public SourceInterface visitDdlStatement(DdlStatementContext ctx) {
		return visit(ctx.children.get(0));
	}
	
	@Override
	public SourceInterface visitCreateIndex(CreateIndexContext ctx) {
		SourceInterface result = new SourceInterface();
		for(ISymbol table: symbolVisitor.visit(ctx.table))
			result.addImport(table);
		return result;
	}
}
