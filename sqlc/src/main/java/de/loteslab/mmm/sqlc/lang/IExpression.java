package de.loteslab.mmm.sqlc.lang;

public interface IExpression extends IValidatable, IScriptPart {
	ExpressionType getExpressionType();
	Object evaluate();
}
