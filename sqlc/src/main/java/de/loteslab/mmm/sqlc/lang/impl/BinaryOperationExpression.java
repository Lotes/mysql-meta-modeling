package de.loteslab.mmm.sqlc.lang.impl;

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
	private IBinaryOperation operation = null;
	
	public BinaryOperationExpression(ParserRuleContext ctx, BinaryOperator operator, IExpression lhs, IExpression rhs) {
		this.ctx = ctx;
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public ValidationResult validate() {
		final ValidationResult ok = new ValidationResult();
		final ExpressionType left = lhs.getExpressionType();
		final ExpressionType right = rhs.getExpressionType();
		switch(operator) {
		case ADD:
			break;
		case BAND: 
			break;
		case BOR:	
			break;
		case DIVIDE:
			break;
		case EQ:
			break;
		case GE:
			break;
		case GT:
			break;
		case LAND:
			break;
		case LE:
			break;
		case LOR:
			break;
		case LT:
			break;
		case MODULO:
			break;
		case MULTIPLY:
			break;
		case NEQ:
			break;
		case SHL:
			if(left != right)
				return new ValidationResult("SHL expects operands of the same type.");
			if(left != ExpressionType.NUMBER)
				return new ValidationResult("SHL only expects NUMBER operands.");
			operation = new IBinaryOperation() {
				@Override
				public Object evaluate(Object lhs, Object rhs) {
					return ((Double)lhs).intValue() << ((Double)rhs).intValue();
				}
			};
			break;
		case SHR: 
			if(left != right)
				return new ValidationResult("SHR expects operands of the same type.");
			if(left != ExpressionType.NUMBER)
				return new ValidationResult("SHR only expects NUMBER operands.");
			operation = new IBinaryOperation() {
				@Override
				public Object evaluate(Object lhs, Object rhs) {
					return ((Double)lhs).intValue() >> ((Double)rhs).intValue();
				}
			};
			break;
		case SUBTRACT:
			if(left != right)
				return new ValidationResult("SUBTRACT expects operands of the same type.");
			if(left != ExpressionType.NUMBER)
				return new ValidationResult("SUBTRACT only expects NUMBER operands.");
			operation = new IBinaryOperation() {
				@Override
				public Object evaluate(Object lhs, Object rhs) {
					return ((Double)lhs)-((Double)rhs);
				}
			};
			break;
		case XOR:
			if(left != right)
				return new ValidationResult("XOR expects operands of the same type.");
			if(left != ExpressionType.BOOLEAN)
				return new ValidationResult("XOR only accepts BOOLEAN operands.");
			operation = new IBinaryOperation() {
				@Override
				public Object evaluate(Object lhs, Object rhs) {
					return ((Boolean)lhs)^((Boolean)rhs);
				}
			};
		default:
			operation = null;
			return new ValidationResult("Unknown operator: "+operator);
		}
		return ok;
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

	@Override
	public ParserRuleContext getRuleContext() {
		return ctx;
	}

}
