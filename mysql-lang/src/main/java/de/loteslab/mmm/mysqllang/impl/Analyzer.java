package de.loteslab.mmm.mysqllang.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import de.loteslab.mmm.mysqllang.IAnalyzer;
import de.loteslab.mmm.mysqllang.IParser;
import de.loteslab.mmm.mysqllang.IPreprocessedSource;
import de.loteslab.mmm.mysqllang.ISourceInterface;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;

public class Analyzer implements IAnalyzer {	
	private IParser parser;
	private ISymbolFactory factory;
	
	public Analyzer(IParser parser, ISymbolFactory symbolFactory) {
		this.factory = symbolFactory;
		this.parser = parser;
	}

	@Override
	public ISourceInterface analyzeSource(IPreprocessedSource source) {
		InputStream stream = new ByteArrayInputStream(source.getPreprocessedContent().getBytes(StandardCharsets.UTF_8));
		try {
			SourceInterfaceVisitor visitor = new SourceInterfaceVisitor(factory);
			RootContext root = parser.parse(stream);
			return visitor.visit(root);
		} catch (IOException e) {
			return null;
		}
	}
}
