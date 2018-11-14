package de.loteslab.mmm.sqlc.lang;

public interface IStatement extends IValidatable  {
	void execute(IExecutionContext context);
}
