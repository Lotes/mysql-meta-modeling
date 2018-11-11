package de.loteslab.mmm.mysqllang.impl;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolType;
import de.loteslab.mmm.mysqllang.ScopeAction;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.AdministrationStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.AlterDatabaseSimpleContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.AlterDatabaseUpgradeNameContext;
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
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropDatabaseContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropEventContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropFunctionContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropIndexContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropLogfileGroupContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropProcedureContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropServerContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropTableContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DropTablespaceContext;
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
import de.loteslab.mmm.mysqllang.internal.MySqlParser.TransactionStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UtilityStatementContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SourceExecutionVisitor extends MySqlParserBaseVisitor<SourceExecution> {
	private SymbolFactory factory;
	private SymbolsVisitor symbolVisitor;
	private TextVisitor textVisitor;
	
	public SourceExecutionVisitor(SymbolFactory factory) {
		this.factory = factory;
		this.textVisitor = new TextVisitor();
		this.symbolVisitor = new SymbolsVisitor(factory, textVisitor);
	}
	
	@Override
	public SourceExecution visitRoot(RootContext ctx) {
		return visit(ctx.stmts);
	}
	
	@Override
	public SourceExecution visitSqlStatements(SqlStatementsContext ctx) {
		SourceExecution result = new SourceExecution();
		for(NonLastStatementContext stmt: ctx.nonLasts) {
			SourceExecution exec = this.visit(stmt);
			result.addExecution(exec);
		}
		result.addExecution(this.visit(ctx.last));
		return result;
	}
	
	@Override
	public SourceExecution visitLastStatementEmpty(LastStatementEmptyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitNonLastStatementEmpty(NonLastStatementEmptyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitLastStatementSql(LastStatementSqlContext ctx) {
		return visit(ctx.stmt);
	}
	
	@Override
	public SourceExecution visitNonLastStatementSql(NonLastStatementSqlContext ctx) {
		return visit(ctx.stmt);
	}
	
	@Override
	public SourceExecution visitEmptyStatement(EmptyStatementContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitSqlStatement(SqlStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitDdlStatement(DdlStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	 @Override
	public SourceExecution visitDmlStatement(DmlStatementContext ctx) {
		 return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
		String id = textVisitor.visit(ctx.id);
		ISymbolType type = factory.Predefined.LogFileGroup;
		ISymbol symbol = factory.createSymbol(id, type, SourceExecution.EMPTY);
		SourceExecution result = new SourceExecution();
		result.addAction(ScopeAction.CREATE, symbol);
		return result;
	}
	 
	@Override
	public SourceExecution visitCreateIndex(CreateIndexContext ctx) {
		SourceExecution result = new SourceExecution();

		for(ISymbol table: symbolVisitor.visit(ctx.table))
			result.addAction(ScopeAction.REQUIRE, table);
		
		String indexName = textVisitor.visit(ctx.id);
		ISymbolType indexType = factory.Predefined.Index;
		ISymbol indexSymbol = factory.createSymbol(indexName, indexType, SourceExecution.EMPTY);
		result.addAction(ScopeAction.CREATE, indexSymbol);
		
		return result;
	}
	
	@Override
	public SourceExecution visitTransactionStatement(TransactionStatementContext ctx) {
		return takeFirst(ctx);
	}

	@Override
	public SourceExecution visitReplicationStatement(ReplicationStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitPreparedStatement(PreparedStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitAdministrationStatement(AdministrationStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitCompoundStatement(CompoundStatementContext ctx) {
		return takeFirst(ctx);
	}
	
	@Override
	public SourceExecution visitUtilityStatement(UtilityStatementContext ctx) {
		return takeFirst(ctx);
	}
		
	@Override
	public SourceExecution visitCreateDatabase(CreateDatabaseContext ctx) {
		SourceExecution result = new SourceExecution();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = factory.Predefined.Database;
		ISymbol symbol =factory.createSymbol(name, type, SourceExecution.EMPTY);
		result.addAction(ScopeAction.CREATE, symbol);
		return result;
	}
	
	@Override
	public SourceExecution visitCreateEvent(CreateEventContext ctx) {
		SourceExecution result = new SourceExecution();
		
		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addAction(ScopeAction.REQUIRE, user);
			}
		}
		
		SourceExecution body = visit(ctx.body);
		String eventName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.Predefined.Event;
		ISymbol symbolEvent = factory.createSymbol(eventName, type, body);
		result.addAction(ScopeAction.CREATE, symbolEvent);
		
		return result;
	}
	
	@Override
	public SourceExecution visitCreateProcedure(CreateProcedureContext ctx) {
		SourceExecution result = new SourceExecution();

		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addAction(ScopeAction.REQUIRE, user);
			}
		}
		
		SourceExecution body = visit(ctx.body);
		String procedureName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.Predefined.Procedure;
		ISymbol symbolProcedure = factory.createSymbol(procedureName, type, body);
		result.addAction(ScopeAction.CREATE, symbolProcedure);

		return result;
	}
	
	@Override
	public SourceExecution visitCreateFunction(CreateFunctionContext ctx) {
		SourceExecution result = new SourceExecution();
		
		if(ctx.owner != null) {
			for(ISymbol user: symbolVisitor.visit(ctx.owner)) {
				result.addAction(ScopeAction.REQUIRE, user);
			}
		}
		
		SourceExecution body = visit(ctx.body);
		String functionName = textVisitor.visit(ctx.name);
		ISymbolType type = factory.Predefined.Function;
		ISymbol symbolFunction = factory.createSymbol(functionName, type, body);
		result.addAction(ScopeAction.CREATE, symbolFunction);
		
		return result;
	}
	
	@Override
	public SourceExecution visitCreateServer(CreateServerContext ctx) {
		SourceExecution result = new SourceExecution();
		String name = textVisitor.visit(ctx.name);
		ISymbolType type = factory.Predefined.Server;
		ISymbol server = factory.createSymbol(name, type, SourceExecution.EMPTY);
		result.addAction(ScopeAction.CREATE, server);
		return result;
	}
	
	private SourceExecution takeFirst(ParserRuleContext ctx) {
		return visit(ctx.children.get(0));
	}
	
	@Override
	public SourceExecution visitLikeTableWithoutParentheses(LikeTableWithoutParenthesesContext ctx) {
		SourceExecution result = new SourceExecution();
		for(ISymbol table: symbolVisitor.visit(ctx.name))
			result.addAction(ScopeAction.REQUIRE, table);
		return result;
	}
	
	@Override
	public SourceExecution visitCreateTableLike(CreateTableLikeContext ctx) {
		SourceExecution result = new SourceExecution();
		for(ISymbol table: symbolVisitor.visit(ctx.original))
			result.addAction(ScopeAction.REQUIRE, table);
		for(ISymbol table: symbolVisitor.visit(ctx.name))
			result.addAction(ScopeAction.CREATE, table);
		return result;
	}
	
	@Override
	public SourceExecution visitCreateDefinitions(CreateDefinitionsContext ctx) {
		SourceExecution result = new SourceExecution();
		for(CreateDefinitionContext def: ctx.defs)
			result.addExecution(visit(def));
		return result;
	}
	
	@Override
	public SourceExecution visitCreateDefinitionColumn(CreateDefinitionColumnContext ctx) {
		return visit(ctx.def);
	}
	
	@Override
	public SourceExecution visitColumnDefinition(ColumnDefinitionContext ctx) {
		SourceExecution result = new SourceExecution();
		for(ColumnConstraintContext colConstraint: ctx.constraints)
			result.addExecution(visit(colConstraint));
		return result;
	}
	
	@Override
	public SourceExecution visitColumnConstraintAutoIncrement(ColumnConstraintAutoIncrementContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintComment(ColumnConstraintCommentContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintDefault(ColumnConstraintDefaultContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintFormat(ColumnConstraintFormatContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintNull(ColumnConstraintNullContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintPrimaryKey(ColumnConstraintPrimaryKeyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintStorage(ColumnConstraintStorageContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintUniqueKey(ColumnConstraintUniqueKeyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitColumnConstraintReference(ColumnConstraintReferenceContext ctx) {
		return visit(ctx.refs);
	}
	
	@Override
	public SourceExecution visitReferenceDefinition(ReferenceDefinitionContext ctx) {
		SourceExecution result = new SourceExecution();
		for(ISymbol table: symbolVisitor.visit(ctx.name))
			result.addAction(ScopeAction.REQUIRE, table);
		return result;
	}
	
	@Override
	public SourceExecution visitCreateDefinitionConstraint(CreateDefinitionConstraintContext ctx) {
		return visit(ctx.constraint);
	}
	
	@Override
	public SourceExecution visitTableConstraintCheck(TableConstraintCheckContext ctx) {
		return visit(ctx.exp);
	}
	
	@Override
	public SourceExecution visitTableConstraintForeignKey(TableConstraintForeignKeyContext ctx) {
		return visit(ctx.def);
	}
	
	@Override
	public SourceExecution visitTableConstraintPrimaryKey(TableConstraintPrimaryKeyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitTableConstraintUniqueKey(TableConstraintUniqueKeyContext ctx) {
		return new SourceExecution();
	}
	
	@Override
	public SourceExecution visitCreateTrigger(CreateTriggerContext ctx) {
		SourceExecution result = new SourceExecution();
		ISymbolType type = factory.Predefined.Trigger;
		
		ISymbolType typeTable = factory.Predefined.TableLike;
		String tableName = textVisitor.visit(ctx.table);
		ISymbol table = factory.createSymbol(tableName, typeTable, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, table);
		
		if(ctx.otherTrigger != null) {
			String otherName = textVisitor.visit(ctx.otherTrigger);
			ISymbol otherTrigger = factory.createSymbol(otherName, type, SourceExecution.EMPTY);
			result.addAction(ScopeAction.REQUIRE, otherTrigger);
		}
		
		if(ctx.owner != null) {
			for(ISymbol owner: symbolVisitor.visit(ctx.owner))
				result.addAction(ScopeAction.REQUIRE, owner);
		}
		
		SourceExecution body = visit(ctx.body);
		String name = textVisitor.visit(ctx.thisTrigger);
		ISymbol trigger = factory.createSymbol(name, type, body);
		result.addAction(ScopeAction.CREATE, trigger);
		
		return result;
	}
	
	@Override
	public SourceExecution visitAlterDatabaseSimple(AlterDatabaseSimpleContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = ctx.name != null 
			? textVisitor.visit(ctx.name)
			: SymbolFactory.CURRENT_DB;
		ISymbol database = factory.createSymbol(name, factory.Predefined.Database, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, database);
				
		return result;
	}
	
	@Override
	public SourceExecution visitAlterDatabaseUpgradeName(AlterDatabaseUpgradeNameContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol database = factory.createSymbol(name, factory.Predefined.Database, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, database);
		
		return result;
	}

	@Override
	public SourceExecution visitDropDatabase(DropDatabaseContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol database = factory.createSymbol(name, factory.Predefined.Database, SourceExecution.EMPTY);
		if(ctx.exists == null)
			result.addAction(ScopeAction.REQUIRE, database);
		result.addAction(ScopeAction.DELETE, database);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropEvent(DropEventContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol event = factory.createSymbol(name, factory.Predefined.Event, SourceExecution.EMPTY);
		if(ctx.exists == null)
			result.addAction(ScopeAction.REQUIRE, event);
		result.addAction(ScopeAction.DELETE, event);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropIndex(DropIndexContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol index = factory.createSymbol(name, factory.Predefined.Index, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, index);
		result.addAction(ScopeAction.DELETE, index);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropLogfileGroup(DropLogfileGroupContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol group = factory.createSymbol(name, factory.Predefined.LogFileGroup, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, group);
		result.addAction(ScopeAction.DELETE, group);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropProcedure(DropProcedureContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol procedure = factory.createSymbol(name, factory.Predefined.Procedure, SourceExecution.EMPTY);
		if(ctx.exists == null)
			result.addAction(ScopeAction.REQUIRE, procedure);
		result.addAction(ScopeAction.DELETE, procedure);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropFunction(DropFunctionContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol function = factory.createSymbol(name, factory.Predefined.Function, SourceExecution.EMPTY);
		if(ctx.exists == null)
			result.addAction(ScopeAction.REQUIRE, function);
		result.addAction(ScopeAction.DELETE, function);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropServer(DropServerContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name = textVisitor.visit(ctx.name);
		ISymbol server = factory.createSymbol(name, factory.Predefined.Server, SourceExecution.EMPTY);
		if(ctx.exists == null)
			result.addAction(ScopeAction.REQUIRE, server);
		result.addAction(ScopeAction.DELETE, server);
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropTable(DropTableContext ctx) {
		SourceExecution result = new SourceExecution();
		boolean requires = ctx.exists == null;
		
		for(ISymbol table: symbolVisitor.visit(ctx.tbls)) {
			if(requires)
				result.addAction(ScopeAction.REQUIRE, table);
			result.addAction(ScopeAction.DELETE, table);		
		}
		
		return result;
	}
	
	@Override
	public SourceExecution visitDropTablespace(DropTablespaceContext ctx) {
		SourceExecution result = new SourceExecution();
		
		String name =textVisitor.visit(ctx.name);
		ISymbol symbol = factory.createSymbol(name, factory.Predefined.Tablespace, SourceExecution.EMPTY);
		result.addAction(ScopeAction.REQUIRE, symbol);
		result.addAction(ScopeAction.DELETE, symbol);
		
		return result;
	}
}
