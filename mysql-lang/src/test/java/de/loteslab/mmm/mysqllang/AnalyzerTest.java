package de.loteslab.mmm.mysqllang;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.loteslab.mmm.mysqllang.impl.Analyzer;
import de.loteslab.mmm.mysqllang.impl.ExpectationStatement;
import de.loteslab.mmm.mysqllang.impl.Parser;
import de.loteslab.mmm.mysqllang.impl.SourceExecution;
import de.loteslab.mmm.mysqllang.impl.SymbolFactory;

@RunWith(Parameterized.class)
public class AnalyzerTest {
	/* SAMPLE FILE:
	 * --scope
	 * xxx:TABLE_LIKE
	 * --input
	 * SELECT * FROM table;
	 * --output
	 * CREATED xxx:TABLE_LIKE
	 * NEEDED table:TABLE_LIKE
	 */
	private enum State {
		EMPTY, INPUT, OUTPUT, SCOPE
	}
	
	private static class Source implements IPreprocessedSource {
		private String content;
		
		public Source(String input) {
			this.content = input;
		}
		
		@Override
		public String getPreprocessedContent() {
			return content;
		}
	}
	
	@Parameters(name = "{0}")
    public static Iterable<File> data() throws IOException {
    	File rootFolder = new File("examples/analyze-interface").getCanonicalFile();
    	LinkedList<File> list = new LinkedList<File>();
    	for(File file: rootFolder.listFiles())
    		if(file.isFile())
    			list.add(file);
		return list;
    }
	
	@Parameter(0)
	public File file;
	
	@Test
	public void test() throws FileNotFoundException, IOException, TestCaseSyntaxException {
		//read lines
		SymbolFactory symbolFactory = new SymbolFactory();
		HashSet<ISymbol> scope = new HashSet<ISymbol>();
		StringBuilder inputBuilder = new StringBuilder();
		List<ExpectationStatement> expectations = new LinkedList<ExpectationStatement>();
		readTestCase(symbolFactory, scope, inputBuilder, expectations);
		
		//parse input
		IParser parser = new Parser();
		IAnalyzer analyzer = new Analyzer(parser, symbolFactory);
		IPreprocessedSource source = new Source(inputBuilder.toString());
		ISourceExecution iface = analyzer.analyzeSource(source);
		
		//execute input
		HashSet<IExpectationStatement> target = new HashSet<IExpectationStatement>();
		for(ISymbol symbol: scope)
			target.add(new ExpectationStatement(Expectation.CREATED, symbol));
		for(IExpectationStatement expectation: iface.getExpectations())
			target.add(expectation);
		//compare with expectations
		for(ExpectationStatement exp: expectations)
			if(!target.contains(exp.getSymbol()))
				fail("Expected, but not found: "+exp.getExpectation().name()+" "+exp.getSymbol().getName()+": "+exp.getSymbol().getSymbolType().getName());
	}

	private void readTestCase(ISymbolFactory symbolFactory, HashSet<ISymbol> scope, StringBuilder inputBuilder, List<ExpectationStatement> statements) throws IOException, TestCaseSyntaxException {
		final String patternInput = "^--input$";
		final String patternOutput = "^--output$";
		final String patternScope = "^--scope$";
		final String patternExpectation = "^([a-z_0-9]+) ([^:]+):([^:]+)$";
		final String patternDefintion = "^([^:]+):([^:]+)$";
		final Pattern patternExpectationCompiled = Pattern.compile(patternExpectation, Pattern.CASE_INSENSITIVE);
		final Pattern patternDefinitionCompiled = Pattern.compile(patternDefintion, Pattern.CASE_INSENSITIVE);
		
		State state = State.EMPTY;
		int lineNumber = 1;
		for(String line: Files.readAllLines(file.toPath())) {
			if(line.matches(patternInput)) {
				state = State.INPUT;
			} else if(line.matches(patternOutput)) {
				state = State.OUTPUT;
			} else if(line.matches(patternScope)) {
				state = State.SCOPE;
			} else {
				switch (state) {
				case INPUT:
					inputBuilder.append(line);
					inputBuilder.append("\r\n");
					break;
				case SCOPE:
					Matcher scopeMatcher = patternDefinitionCompiled.matcher(line);
					if(scopeMatcher.find()) {
						String strName = scopeMatcher.group(1);
						String strType = scopeMatcher.group(2);
						ISymbolType type = symbolFactory.createSymbolType(strType);
						ISymbol symbol = symbolFactory.createSymbol(strName, type, SourceExecution.EMPTY);
						scope.add(symbol);
					} else {
						ThrowFormatError(lineNumber, line);
					}
					break;
				case OUTPUT:
					Matcher matcher = patternExpectationCompiled.matcher(line);
					if(matcher.find()) {
						String strExp = matcher.group(1);
						String strName = matcher.group(2);
						String strType = matcher.group(3);
						Expectation expectation = Expectation.valueOf(strExp);
						ISymbolType type = symbolFactory.createSymbolType(strType);
						ISymbol symbol = symbolFactory.createSymbol(strName, type, SourceExecution.EMPTY);
						ExpectationStatement statement = new ExpectationStatement(expectation, symbol);
						statements.add(statement);
					} else {
						ThrowFormatError(lineNumber, line);
					}
					break;
				default:
					break;
				}
			}
			lineNumber++;
		}
	}

	private void ThrowFormatError(int lineNumber, String line) throws TestCaseSyntaxException {
		throw new TestCaseSyntaxException("Unexpected format at line "+lineNumber+": "+line);
	}
}