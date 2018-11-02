package de.loteslab.mmm.mysqllang.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import de.loteslab.mmm.mysqllang.IAnalyzer;
import de.loteslab.mmm.mysqllang.IParser;
import de.loteslab.mmm.mysqllang.IPreprocessedSource;
import de.loteslab.mmm.mysqllang.ISourceExecution;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;

public class Analyzer implements IAnalyzer {	
	private IParser parser;
	private SymbolFactory factory;
	
	public Analyzer(IParser parser, SymbolFactory symbolFactory) {
		this.factory = symbolFactory;
		this.parser = parser;
	}

	@Override
	public ISourceExecution analyzeSource(IPreprocessedSource source) {
		InputStream stream = new ByteArrayInputStream(source.getPreprocessedContent().getBytes(StandardCharsets.UTF_8));
		try {
			SourceExecutionVisitor visitor = new SourceExecutionVisitor(factory);
			RootContext root = parser.parse(stream);
			return visitor.visit(root);
		} catch (IOException e) {
			return null;
		}
	}
}
