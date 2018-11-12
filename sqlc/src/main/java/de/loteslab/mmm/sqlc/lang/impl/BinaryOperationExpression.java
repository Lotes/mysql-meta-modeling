package de.loteslab.mmm.sqlc.lang.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import com.mysql.cj.jdbc.exceptions.OperationNotSupportedException;

import de.loteslab.mmm.sqlc.lang.ExpressionType;
import de.loteslab.mmm.sqlc.lang.IExpression;
import de.loteslab.mmm.sqlc.lang.ValidationResult;

public class BinaryOperationExpression implements IExpression {

	private ParserRuleContext ctx;
	private BinaryOperator operator;
	private IExpression lhs;
	private IExpression rhs;
	private IBinaryOperation operation;
	private ExpressionType expressionType;
	
	public BinaryOperationExpression(ParserRuleContext ctx, BinaryOperator operator, IExpression lhs, IExpression rhs) {
		this.ctx = ctx;
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate() {
		ValidationResult childResult = lhs.validate();
		if(!childResult.IsValid())
			return childResult;
		childResult = rhs.validate();
		if(!childResult.IsValid())
			return childResult;
		
		final ValidationResult ok = new ValidationResult();
		final ExpressionType left = lhs.getExpressionType();
		final ExpressionType right = rhs.getExpressionType();
		final List<ExpressionType> NumberOnly = new LinkedList<>();
		final List<ExpressionType> NumberAndBooleanOnly = new LinkedList<>();
		final List<ExpressionType> NumberStringAndBooleanOnly = new LinkedList<>();
		NumberOnly.add(ExpressionType.NUMBER);
		NumberAndBooleanOnly.add(ExpressionType.NUMBER);
		NumberAndBooleanOnly.add(ExpressionType.BOOLEAN);
		NumberStringAndBooleanOnly.add(ExpressionType.NUMBER);
		NumberStringAndBooleanOnly.add(ExpressionType.BOOLEAN);
		NumberStringAndBooleanOnly.add(ExpressionType.STRING);
		ValidationResult tempResult;
		switch(operator) {
		case ADD:
			if(left == ExpressionType.LIST && right == ExpressionType.LIST) {
				expressionType = ExpressionType.LIST;
				operation = (lhs, rhs) -> 
				{
					List<Object> result = new LinkedList<>();
					Iterable<Object> leftList = (Iterable<Object>)lhs;
					Iterable<Object> rightList = (Iterable<Object>)rhs;
					for(Object item: leftList) result.add(item);
					for(Object item: rightList) result.add(item);
					return result;					
				};
			} else if(left == ExpressionType.STRING && right == ExpressionType.NULL) {
				operation = (lhs, rhs) -> lhs;
				expressionType = ExpressionType.STRING;
			} else if(left == ExpressionType.STRING && right == ExpressionType.BOOLEAN) {
				expressionType = ExpressionType.STRING;
				operation = (lhs, rhs) -> ((String)lhs) + rhs.toString();
			} else if(left == ExpressionType.STRING && right == ExpressionType.NUMBER) {
				expressionType = ExpressionType.STRING;
				operation = (lhs, rhs) -> ((String)lhs) + rhs.toString();
			} else if(left == ExpressionType.STRING && right == ExpressionType.STRING) {
				expressionType = ExpressionType.STRING;
				operation = (lhs, rhs) -> ((String)lhs) + ((String)rhs);
			} else if(left == ExpressionType.NULL && right == ExpressionType.NULL) {
				expressionType = ExpressionType.NULL;
				operation = (lhs, rhs) -> null;
			} else if(left == ExpressionType.NULL && right == ExpressionType.STRING) {
				expressionType = ExpressionType.STRING;
				operation = (lhs, rhs) -> rhs;
			} else {
				operation = null;
				return new ValidationResult(ctx, "PLUS operator is not applicible to given operands!");
			}
			break;
		case EQ:
			operation = (lhs, rhs) -> lhs == rhs;
			expressionType = ExpressionType.BOOLEAN;
			break;
		case NEQ:
			operation = (lhs, rhs) -> lhs != rhs;
			expressionType = ExpressionType.BOOLEAN;
			break;
		case GE:
		case GT:
		case LE:
		case LT:
			tempResult = ExpectBothSameType(left, right, NumberStringAndBooleanOnly);
			if(tempResult != null)
				return tempResult;
			switch(left) {
			case STRING:
				switch(operator) {
				case GE:
					operation = (lhs, rhs) -> ((String)lhs).compareTo((String)rhs) <= 0;
					break;
				case GT:
					operation = (lhs, rhs) -> ((String)lhs).compareTo((String)rhs) < 0;
					break;
				case LE:
					operation = (lhs, rhs) -> ((String)lhs).compareTo((String)rhs) >= 0;
					break;
				case LT:	
					operation = (lhs, rhs) -> ((String)lhs).compareTo((String)rhs) > 0;
					break;
				}
				expressionType = ExpressionType.BOOLEAN;
				break;
			case NUMBER:
				switch(operator) {
				case GE:
					operation = (lhs, rhs) -> ((Double)lhs).compareTo((Double)rhs) <= 0;
					break;
				case GT:
					operation = (lhs, rhs) -> ((Double)lhs).compareTo((Double)rhs) < 0;
					break;
				case LE:
					operation = (lhs, rhs) -> ((Double)lhs).compareTo((Double)rhs) >= 0;
					break;
				case LT:	
					operation = (lhs, rhs) -> ((Double)lhs).compareTo((Double)rhs) > 0;
					break;
				}
				expressionType = ExpressionType.BOOLEAN;
				break;
			case BOOLEAN:
				switch(operator) {
				case GE:
					operation = (lhs, rhs) -> ((Boolean)lhs).compareTo((Boolean)rhs) <= 0;
					break;
				case GT:
					operation = (lhs, rhs) -> ((Boolean)lhs).compareTo((Boolean)rhs) < 0;
					break;
				case LE:
					operation = (lhs, rhs) -> ((Boolean)lhs).compareTo((Boolean)rhs) >= 0;
					break;
				case LT:	
					operation = (lhs, rhs) -> ((Boolean)lhs).compareTo((Boolean)rhs) > 0;
					break;
				}
				expressionType = ExpressionType.BOOLEAN;
				break;
			}
			break;
		case SHL:
		case SHR: 
			tempResult = ExpectBothSameType(left, right, NumberOnly);
			if(tempResult != null)
				return tempResult;
			if(operator == BinaryOperator.SHR)
				operation = (lhs, rhs) -> ((Double)lhs).intValue() >> ((Double)rhs).intValue();
			else
				operation = new IBinaryOperation() {
					@Override
					public Object evaluate(Object lhs, Object rhs) {
						return ((Double)lhs).intValue() << ((Double)rhs).intValue();
					}
				};
				expressionType = ExpressionType.NUMBER;
			break;
		case MULTIPLY:
		case DIVIDE:
		case MODULO:
		case SUBTRACT:
			tempResult = ExpectBothSameType(left, right, NumberOnly);
			if(tempResult != null)
				return tempResult;
			switch(operator) {
			case MULTIPLY:
				operation = (lhs, rhs) -> ((Double)lhs)*((Double)rhs);
				break;
			case DIVIDE:
				operation = (lhs, rhs) -> ((Double)lhs)/((Double)rhs);
				break;
			case MODULO:
				operation = (lhs, rhs) -> ((Double)lhs)%((Double)rhs);
				break;
			case SUBTRACT:	
				operation = (lhs, rhs) -> ((Double)lhs)-((Double)rhs);
				break;
			}
			expressionType = ExpressionType.NUMBER;
			break;
		case XOR:
		case BAND:
		case BOR:
		case LAND:
		case LOR:
			tempResult = ExpectBothSameType(left, right, NumberAndBooleanOnly);
			if(tempResult != null)
				return tempResult;
			if(left == ExpressionType.BOOLEAN) {
				switch(operator) {
				case XOR: operation = (lhs, rhs) -> ((Boolean)lhs)^((Boolean)rhs); break;
				case BAND: operation = (lhs, rhs) -> ((Boolean)lhs)&((Boolean)rhs); break;
				case BOR: operation = (lhs, rhs) -> ((Boolean)lhs)|((Boolean)rhs); break;
				case LAND: operation = (lhs, rhs) -> ((Boolean)lhs)&&((Boolean)rhs); break;
				case LOR: operation = (lhs, rhs) -> ((Boolean)lhs)||((Boolean)rhs); break;
				}
				expressionType = ExpressionType.BOOLEAN;
			} else {
				switch(operator) {
				case XOR: operation = (lhs, rhs) -> ((Double)lhs).intValue()^((Double)rhs).intValue(); break;
				case BAND: operation = (lhs, rhs) -> ((Double)lhs).intValue()&((Double)rhs).intValue(); break;
				case BOR: operation = (lhs, rhs) -> ((Double)lhs).intValue()|((Double)rhs).intValue(); break;
				case LAND: operation = (lhs, rhs) -> ((Double)lhs).intValue()&((Double)rhs).intValue(); break;
				case LOR: operation = (lhs, rhs) -> ((Double)lhs).intValue()|((Double)rhs).intValue(); break;
				}
				expressionType = ExpressionType.NUMBER;
			}				
			break;
		default:
			operation = null;
			return new ValidationResult(ctx, "Unknown operator: "+operator);
		}
		return ok;
	}

	private ValidationResult ExpectBothSameType(ExpressionType left, ExpressionType right, List<ExpressionType> types)
	{
		if(left != right)
			return new ValidationResult(ctx, "Expects operands of the same type.");
		if(types.indexOf(left) == -1)
			return new ValidationResult(ctx, "Only accepts "+String.join(", ", types.stream().map(t -> t.name()).collect(Collectors.toList()))+" operands.");
		return null;
	}
	
	@Override
	public ExpressionType getExpressionType() {
		return expressionType;
	}

	@Override
	public Object evaluate() {
		Object left = null;
		Object right = null;
		//TODO
		return operation.evaluate(left, right);
	}

	@Override
	public ParserRuleContext getRuleContext() {
		return ctx;
	}
}
