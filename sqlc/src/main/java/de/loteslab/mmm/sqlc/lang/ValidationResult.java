package de.loteslab.mmm.sqlc.lang;

import org.antlr.v4.runtime.ParserRuleContext;

public final class ValidationResult {
	private boolean valid;
	private String message;
	
	public ValidationResult() {
		valid = true;
		message = null;
	}
	public ValidationResult(String message) {
		valid = false;
		this.message = message;
	}
	public boolean IsValid() {
		return valid;
	}
	public String getMEssage() {
		return message;
	}
}
