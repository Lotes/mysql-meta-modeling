package de.loteslab.mmm.sqlc.lang.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.loteslab.mmm.lang.MacroParser.AdditiveExpansionContext;
import de.loteslab.mmm.lang.MacroParser.AdditiveNextContext;
import de.loteslab.mmm.lang.MacroParser.AndExpansionContext;
import de.loteslab.mmm.lang.MacroParser.AndNextContext;
import de.loteslab.mmm.lang.MacroParser.ConditionalExpressionContext;
import de.loteslab.mmm.lang.MacroParser.DefineContext;
import de.loteslab.mmm.lang.MacroParser.DefinitionBodyAssignContext;
import de.loteslab.mmm.lang.MacroParser.DefinitionBodyContext;
import de.loteslab.mmm.lang.MacroParser.DefinitionBodyLoadContext;
import de.loteslab.mmm.lang.MacroParser.EqualityExpansionContext;
import de.loteslab.mmm.lang.MacroParser.EqualityNextContext;
import de.loteslab.mmm.lang.MacroParser.ExpressionContext;
import de.loteslab.mmm.lang.MacroParser.ExpressionListContext;
import de.loteslab.mmm.lang.MacroParser.ImportingContext;
import de.loteslab.mmm.lang.MacroParser.JArrayContext;
import de.loteslab.mmm.lang.MacroParser.JObjectContext;
import de.loteslab.mmm.lang.MacroParser.LandExpansionContext;
import de.loteslab.mmm.lang.MacroParser.LandNextContext;
import de.loteslab.mmm.lang.MacroParser.LorExpansionContext;
import de.loteslab.mmm.lang.MacroParser.LorNextContext;
import de.loteslab.mmm.lang.MacroParser.MultiplicativeExpansionContext;
import de.loteslab.mmm.lang.MacroParser.MultiplicativeNextContext;
import de.loteslab.mmm.lang.MacroParser.OrExpansionContext;
import de.loteslab.mmm.lang.MacroParser.OrNextContext;
import de.loteslab.mmm.lang.MacroParser.PairContext;
import de.loteslab.mmm.lang.MacroParser.PairNameContext;
import de.loteslab.mmm.lang.MacroParser.PairsContext;
import de.loteslab.mmm.lang.MacroParser.PostfixArrayContext;
import de.loteslab.mmm.lang.MacroParser.PostfixFunctionContext;
import de.loteslab.mmm.lang.MacroParser.PostfixMemberContext;
import de.loteslab.mmm.lang.MacroParser.PostfixNextContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryArrayContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryFalseContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryIdContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryNullContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryNumberContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryObjectContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryParenthesesContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryStringContext;
import de.loteslab.mmm.lang.MacroParser.PrimaryTrueContext;
import de.loteslab.mmm.lang.MacroParser.RelationalExpansionContext;
import de.loteslab.mmm.lang.MacroParser.RelationalNextContext;
import de.loteslab.mmm.lang.MacroParser.ShiftExpansionContext;
import de.loteslab.mmm.lang.MacroParser.ShiftNextContext;
import de.loteslab.mmm.lang.MacroParser.StartContext;
import de.loteslab.mmm.lang.MacroParser.UnaryExpansionContext;
import de.loteslab.mmm.lang.MacroParser.UnaryNextContext;
import de.loteslab.mmm.lang.MacroParser.XorExpansionContext;
import de.loteslab.mmm.lang.MacroParser.XorNextContext;
import de.loteslab.mmm.lang.MacroParserBaseVisitor;
import de.loteslab.mmm.sqlc.lang.IExpression;

public class ExpressionVisitor extends MacroParserBaseVisitor<IExpression> {
	@Override
	public IExpression visitStart(StartContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitConditionalExpression(ConditionalExpressionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitLorExpansion(LorExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitLorNext(LorNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitLandNext(LandNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitLandExpansion(LandExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitOrNext(OrNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitOrExpansion(OrExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitXorExpansion(XorExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitXorNext(XorNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitAndNext(AndNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitAndExpansion(AndExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitEqualityExpansion(EqualityExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitEqualityNext(EqualityNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitRelationalExpansion(RelationalExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitRelationalNext(RelationalNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitShiftNext(ShiftNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitShiftExpansion(ShiftExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitAdditiveNext(AdditiveNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitAdditiveExpansion(AdditiveExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitMultiplicativeNext(MultiplicativeNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitMultiplicativeExpansion(MultiplicativeExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitUnaryExpansion(UnaryExpansionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitUnaryNext(UnaryNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPostfixMember(PostfixMemberContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPostfixArray(PostfixArrayContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPostfixNext(PostfixNextContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPostfixFunction(PostfixFunctionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryObject(PrimaryObjectContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryArray(PrimaryArrayContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryId(PrimaryIdContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryString(PrimaryStringContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryNumber(PrimaryNumberContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryTrue(PrimaryTrueContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryFalse(PrimaryFalseContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryNull(PrimaryNullContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitPrimaryParentheses(PrimaryParenthesesContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitExpressionList(ExpressionListContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitImporting(ImportingContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitDefine(DefineContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression visitDefinitionBodyAssign(DefinitionBodyAssignContext ctx) {
		return visit(ctx.rvalue);
	}
	
	@Override
	public IExpression visitDefinitionBodyLoad(DefinitionBodyLoadContext ctx) {
		return new LoadFileExpression(ctx, extractString(ctx.fileName.getText()));
	}
	
	private String extractName(PairNameContext ctx) {
		String value = ctx.getText();
		if(value.startsWith("\""))
			return extractString(value);
		return value;
	}
	
	private String extractString(String value) {
		StringBuilder builder = new StringBuilder(value.length());
		int index = 1;
		while(index < value.length()-1) {
			char c = value.charAt(index);
			if(c > 0x1F) {
				builder.append(c);
			} else if(c == '\\') {
				c = value.charAt(++index);
				switch(c) {
				case '"': builder.append('"'); break;
				case '\\': builder.append('\\'); break;
				case '/': builder.append('/'); break;
				case 'b': builder.append('\b'); break;
				case 'f': builder.append('\f'); break;
				case 'n': builder.append('\n'); break;
				case 'r': builder.append('\r'); break;
				case 'u':
					String hex = ""+value.charAt(++index)
						+ value.charAt(++index)
						+ value.charAt(++index)
						+ value.charAt(++index);
					c = (char)Integer.parseInt(hex, 16);
					builder.append(c);
					break;
				}
			}
			index++;
		}
		return builder.toString();
	}

	@Override
	public IExpression visitJObject(JObjectContext ctx) {
		return new ObjectExpression(
			ctx,
			ctx.list.list.stream()
				.map(pair -> new KeyValuePair<String, IExpression>(
					extractName(pair.name), 
					visit(pair.value))
				)
		);
	}
	
	@Override
	public IExpression visitJArray(JArrayContext ctx) {
		List<IExpression> array = ctx.exprs.children.stream().map(c -> visit(c)).collect(Collectors.toList());
		return new ListExpression(ctx, array);
	}

}
