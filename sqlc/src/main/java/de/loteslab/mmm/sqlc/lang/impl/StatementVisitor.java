package de.loteslab.mmm.sqlc.lang.impl;

import de.loteslab.mmm.lang.MacroParserBaseVisitor;
import de.loteslab.mmm.lang.MacroParser.DefineContext;
import de.loteslab.mmm.lang.MacroParser.ImportingContext;
import de.loteslab.mmm.sqlc.lang.IExpression;
import de.loteslab.mmm.sqlc.lang.IStatement;

public class StatementVisitor extends MacroParserBaseVisitor<IStatement>{
	private ExpressionVisitor expressionVisitor = new ExpressionVisitor();
	
	@Override
	public IStatement visitImporting(ImportingContext ctx) {
		String fileName = Utilities.extractString(ctx.fileName.getText());
		return new ImportFileStatement(fileName);
	}
	
	@Override
	public IStatement visitDefine(DefineContext ctx) {
		String variableName = ctx.name.getText();
		IExpression expression = expressionVisitor.visit(ctx.body);
		return new DefineStatement(variableName, expression);
	}
}
