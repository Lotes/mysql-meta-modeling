package de.loteslab.mmm.sqlc.lang.impl;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.lang.MacroParser.JArrayContext;
import de.loteslab.mmm.sqlc.lang.ExpressionType;
import de.loteslab.mmm.sqlc.lang.IExpression;
import de.loteslab.mmm.sqlc.lang.ValidationResult;

public class ListExpression implements IExpression {

	public ListExpression(JArrayContext ctx, List<IExpression> array) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ValidationResult validate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParserRuleContext getRuleContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionType getExpressionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
