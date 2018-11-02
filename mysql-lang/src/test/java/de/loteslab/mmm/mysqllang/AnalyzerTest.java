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
	 * --input
	 * SELECT * FROM table;
	 * --output
	 * IMPORTS table:TABLE_OR_VIEW
	 */
	private enum State {
		EMPTY, INPUT, OUTPUT
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
		StringBuilder inputBuilder = new StringBuilder();
		List<ExpectationStatement> expectations = new LinkedList<ExpectationStatement>();
		readTestCase(symbolFactory, inputBuilder, expectations);
		
		//parse input
		IParser parser = new Parser();
		IAnalyzer analyzer = new Analyzer(parser, symbolFactory);
		IPreprocessedSource source = new Source(inputBuilder.toString());
		ISourceExecution iface = analyzer.analyzeSource(source);
		
		//execute input
		HashSet<IExpectationStatement> target = new HashSet<IExpectationStatement>();
		for(IExpectationStatement expectation: iface.getExpectations())
			target.add(expectation);
		//compare with expectations
		for(ExpectationStatement exp: expectations)
			if(!target.contains(exp.getSymbol()))
				fail("Expected, but not found: "+exp.getExpectation().name()+" "+exp.getSymbol().getName()+": "+exp.getSymbol().getSymbolType().getName());
	}

	private void readTestCase(ISymbolFactory symbolFactory, StringBuilder inputBuilder, List<ExpectationStatement> statements) throws IOException, TestCaseSyntaxException {
		final String patternInput = "^--input$";
		final String patternOutput = "^--output$";
		final String patternSymbol = "^(imports|exports) ([^:]+):([^:]+)$";
		final Pattern patternSymbolCompiled = Pattern.compile(patternSymbol, Pattern.CASE_INSENSITIVE);
		
		State state = State.EMPTY;
		int lineNumber = 1;
		for(String line: Files.readAllLines(file.toPath())) {
			if(line.matches(patternInput)) {
				state = State.INPUT;
			} else if(line.matches(patternOutput)) {
				state = State.OUTPUT;
			} else {
				switch (state) {
				case INPUT:
					inputBuilder.append(line);
					inputBuilder.append("\r\n");
					break;
				case OUTPUT:
					Matcher matcher = patternSymbolCompiled.matcher(line);
					if(matcher.find()) {
						String strMode = matcher.group(1);
						String strName = matcher.group(2);
						String strType = matcher.group(3);
						Expectation expectation = Expectation.valueOf(strMode);
						ISymbolType type = symbolFactory.createSymbolType(strType);
						ISymbol symbol = symbolFactory.createSymbol(strName, type, SourceExecution.EMPTY);
						ExpectationStatement statement = new ExpectationStatement(expectation, symbol);
						statements.add(statement);
					} else {
						throw new TestCaseSyntaxException("Unexpected format at line "+lineNumber+": "+line);
					}
					break;
				default:
					break;
				}
			}
			lineNumber++;
		}
	}
}