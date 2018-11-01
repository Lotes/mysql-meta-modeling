package de.loteslab.mmm.mysqllang.impl;

import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.DottedIdLiteralContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.FullIdContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UidContext;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.UserNameContext;

public class TextVisitor extends MySqlParserBaseVisitor<String>{
	@Override
	public String visitUid(UidContext ctx) {
		String text = ctx.getText();
		return removeQuotes(text);
	}

	@Override
	public String visitDottedIdLiteral(DottedIdLiteralContext ctx) {
		String text = ctx.getText().substring(1);
		return "."+removeQuotes(text);
	}
	
	private String removeQuotes(String text) {
		if(text.matches("^`[^`]+`$") 
				|| text.matches("^'.+'$")
				|| text.matches("^\".+\"$"))
			text = text.substring(0, text.length()-1);
		return text;
	}
	
	@Override
	public String visitUserName(UserNameContext ctx) {
		String name = ctx.getText();
		name.replaceAll("[\"'`]", "");
		return name;
	}
	
	@Override
	public String visitFullId(FullIdContext ctx) {
		String first = visit(ctx.first);
		if(ctx.last == null)
			return first;
		String last = visit(ctx.last);
		return first+"."+last;
	}
}
