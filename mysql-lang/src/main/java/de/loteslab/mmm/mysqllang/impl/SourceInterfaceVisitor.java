package de.loteslab.mmm.mysqllang.impl;

import java.util.LinkedList;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolType;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.AdministrationStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintAutoIncrementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintCommentContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintDefaultContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintFormatContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintNullContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintPrimaryKeyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintReferenceContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintStorageContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnConstraintUniqueKeyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ColumnDefinitionContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CompoundStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateDatabaseContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateDefinitionColumnContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateDefinitionConstraintContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateDefinitionContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateDefinitionsContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateEventContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateFunctionContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateIndexContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateLogfileGroupContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateProcedureContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateServerContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateTableLikeContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.CreateTriggerContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DdlStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DmlStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.EmptyStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LastStatementEmptyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LastStatementSqlContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.LikeTableWithoutParenthesesContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementEmptyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.NonLastStatementSqlContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.PreparedStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ReferenceDefinitionContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.ReplicationStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.SqlStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.SqlStatementsContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableConstraintCheckContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableConstraintForeignKeyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableConstraintPrimaryKeyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableConstraintUniqueKeyContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TableNameContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TablesContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TransactionStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UtilityStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SourceInterfaceVisitor extends MySqlParserBaseVisitor<SourceInterface> {
	private ISymbolFactory factory;
	private SymbolsVisitor symbolVisitor;
	private TextVisitor textVisitor;
	
	public SourceInterfaceVisitor(ISymbolFactory factory) {
		this.factory = factory;
		this.textVisitor = new TextVisitor();
		this.symbolVisitor = new SymbolsVisitor(factory, textVisitor);
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
	public SourceInterface visitEmptyStatement(EmptyStatementContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitSqlStatement(SqlStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitDdlStatement(DdlStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	 @Override
	public SourceInterface visitDmlStatement(DmlStatementContext ctx) {
		 return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
		String id = textVisitor.visit(ctx.id);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.LOGFILE_GROUP);
		ISymbol symbol = factory.createSymbol(id, type);
		SourceInterface result = new SourceInterface();
		result.addExport(symbol);
		return result;
	}
	 
	@Override
	public SourceInterface visitCreateIndex(CreateIndexContext ctx) {
		SourceInterface result = new SourceInterface();
		
		String indexName = textVisitor.visit(ctx.id);
		ISymbolType indexType = factory.createSymbolType(SymbolFactory.TypeNames.INDEX);
		ISymbol indexSymbol = factory.createSymbol(indexName, indexType);
		result.addExport(indexSymbol);
		
		for(ISymbol table: symbolVisitor.visit(ctx.table))
			result.addImport(table);
		
		return result;
	}
	
	@Override
	public SourceInterface visitTransactionStatement(TransactionStatementContext ctx) {
		return takeFirst(ctx);
	}

	@Override
	public SourceInterface visitReplicationStatement(ReplicationStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitPreparedStatement(PreparedStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitAdministrationStatement(AdministrationStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitCompoundStatement(CompoundStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceInterface visitUtilityStatement(UtilityStatementContext ctx) {
		return takeFirst(ctx);
	}
		
	@Override
	public SourceInterface visitCreateDatabase(CreateDatabaseContext ctx) {
		SourceInterface result = new SourceInterface();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.DATABASE);
		ISymbol symbol =factory.createSymbol(name, type);
		result.addExport(symbol);
		return result;
	}
	
	@Override
	public SourceInterface visitCreateEvent(CreateEventContext ctx) {
		SourceInterface result = new SourceInterface();
		String eventName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.EVENT);
		ISymbol symbolEvent =factory.createSymbol(eventName, type);
		result.addExport(symbolEvent);
		
		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addImport(user);
			}
		}
		
		SourceInterface body = visit(ctx.body);
		result.addInterface(body);
		
		return result;
	}
	
	@Override
	public SourceInterface visitCreateProcedure(CreateProcedureContext ctx) {
		SourceInterface result = new SourceInterface();
		String procedureName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.PROCEDURE);
		ISymbol symbolProcedure = factory.createSymbol(procedureName, type);
		result.addExport(symbolProcedure);
		
		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addImport(user);
			}
		}
		
		SourceInterface body = visit(ctx.body);
		result.addInterface(body);
		
		return result;
	}
	
	@Override
	public SourceInterface visitCreateFunction(CreateFunctionContext ctx) {
		SourceInterface result = new SourceInterface();
		String functionName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.FUNCTION);
		ISymbol symbolFunction = factory.createSymbol(functionName, type);
		result.addExport(symbolFunction);
		
		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addImport(user);
			}
		}
		
		SourceInterface body = visit(ctx.body);
		result.addInterface(body);
		
		return result;
	}
	
	@Override
	public SourceInterface visitCreateServer(CreateServerContext ctx) {
		SourceInterface result = new SourceInterface();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.SERVER);
		ISymbol server = factory.createSymbol(name, type);
		result.addExport(server);
		return result;
	}
	
	private SourceInterface takeFirst(ParserRuleContext ctx) {
		return visit(ctx.children.get(0));
	}
	
	@Override
	public SourceInterface visitLikeTableWithoutParentheses(LikeTableWithoutParenthesesContext ctx) {
		SourceInterface result = new SourceInterface();
		for(ISymbol symbol: symbolVisitor.visit(ctx.name))
			result.addImport(symbol);
		return result;
	}
	
	@Override
	public SourceInterface visitCreateTableLike(CreateTableLikeContext ctx) {
		SourceInterface result = new SourceInterface();
		for(ISymbol table: symbolVisitor.visit(ctx.name))
			result.addExport(table);
		for(ISymbol table: symbolVisitor.visit(ctx.original))
			result.addImport(table);
		return result;
	}
	
	@Override
	public SourceInterface visitCreateDefinitions(CreateDefinitionsContext ctx) {
		SourceInterface result = new SourceInterface();
		for(CreateDefinitionContext def: ctx.defs)
			result.addInterface(visit(def));
		return result;
	}
	
	@Override
	public SourceInterface visitCreateDefinitionColumn(CreateDefinitionColumnContext ctx) {
		return visit(ctx.def);
	}
	
	@Override
	public SourceInterface visitColumnDefinition(ColumnDefinitionContext ctx) {
		SourceInterface result = new SourceInterface();
		for(ColumnConstraintContext colConstraint: ctx.constraints)
			result.addInterface(visit(colConstraint));
		return result;
	}
	
	@Override
	public SourceInterface visitColumnConstraintAutoIncrement(ColumnConstraintAutoIncrementContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintComment(ColumnConstraintCommentContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintDefault(ColumnConstraintDefaultContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintFormat(ColumnConstraintFormatContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintNull(ColumnConstraintNullContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintPrimaryKey(ColumnConstraintPrimaryKeyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintStorage(ColumnConstraintStorageContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintUniqueKey(ColumnConstraintUniqueKeyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitColumnConstraintReference(ColumnConstraintReferenceContext ctx) {
		return visit(ctx.refs);
	}
	
	@Override
	public SourceInterface visitReferenceDefinition(ReferenceDefinitionContext ctx) {
		SourceInterface result = new SourceInterface();
		for(ISymbol table: symbolVisitor.visit(ctx.name))
			result.addImport(table);
		return result;
	}
	
	@Override
	public SourceInterface visitCreateDefinitionConstraint(CreateDefinitionConstraintContext ctx) {
		return visit(ctx.constraint);
	}
	
	@Override
	public SourceInterface visitTableConstraintCheck(TableConstraintCheckContext ctx) {
		return visit(ctx.exp);
	}
	
	@Override
	public SourceInterface visitTableConstraintForeignKey(TableConstraintForeignKeyContext ctx) {
		return visit(ctx.def);
	}
	
	@Override
	public SourceInterface visitTableConstraintPrimaryKey(TableConstraintPrimaryKeyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitTableConstraintUniqueKey(TableConstraintUniqueKeyContext ctx) {
		return new SourceInterface();
	}
	
	@Override
	public SourceInterface visitCreateTrigger(CreateTriggerContext ctx) {
		SourceInterface result = new SourceInterface();
		
		String name = textVisitor.visit(ctx.thisTrigger);
		ISymbolType type = factory.createSymbolType(SymbolFactory.TypeNames.TRIGGER);
		ISymbol trigger = factory.createSymbol(name, type);
		result.addExport(trigger);

		ISymbolType typeTable = factory.createSymbolType(SymbolFactory.TypeNames.TABLE_LIKE);
		String tableName = textVisitor.visit(ctx.table);
		ISymbol table = factory.createSymbol(tableName, typeTable);
		result.addImport(table);
		
		if(ctx.otherTrigger != null) {
			String otherName = textVisitor.visit(ctx.otherTrigger);
			ISymbol otherTrigger = factory.createSymbol(otherName, type);
			result.addImport(otherTrigger);
		}
		
		if(ctx.owner != null) {
			for(ISymbol owner: symbolVisitor.visit(ctx.owner))
				result.addImport(owner);
		}
		
		result.addInterface(visit(ctx.body));
		
		return result;
	}
	
}
