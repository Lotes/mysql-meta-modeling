package de.loteslab.mmm.sqlc.lang;

import org.antlr.v4.runtime.ParserRuleContext;

public final class ValidationResult {
	private boolean valid;
	private String message;
	private ParserRuleContext context;
	
	public ValidationResult() {
		valid = true;
		message = null;
	}
	public ValidationResult(ParserRuleContext ctx, String message) {
		valid = false;
		this.message = message;
	}
	public boolean IsValid() {
		return valid;
	}
	public String getMEssage() {
		return message;
	}
	public ParserRuleContext getContext() {
		return context;
	}
}
