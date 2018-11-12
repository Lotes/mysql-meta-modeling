package de.loteslab.mmm.sqlc.lang.impl;

import java.util.stream.Stream;

import org.antlr.v4.runtime.ParserRuleContext;

import de.loteslab.mmm.lang.MacroParser.JObjectContext;
import de.loteslab.mmm.sqlc.lang.ExpressionType;
import de.loteslab.mmm.sqlc.lang.IExpression;
import de.loteslab.mmm.sqlc.lang.ValidationResult;

public class ObjectExpression implements IExpression {

	public ObjectExpression(JObjectContext ctx, Stream<Object> map) {
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
