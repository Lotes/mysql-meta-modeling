package de.loteslab.mmm.mysqllang.impl;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.loteslab.mmm.mysqllang.IParser;
import de.loteslab.mmm.mysqllang.internal.MySqlLexer;
import de.loteslab.mmm.mysqllang.internal.MySqlParser;
import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;

public class Parser implements IParser {

	@Override
	public RootContext parse(InputStream input) throws IOException {
		CharStream stream = CharStreams.fromStream(input);
		MySqlLexer lexer = new MySqlLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MySqlParser parser = new MySqlParser(tokens);
		return parser.root();
	}

}
