package de.loteslab.mmm.mysqllang;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
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
import de.loteslab.mmm.mysqllang.impl.Parser;
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
	
	private enum Mode {
		IMPORTS, EXPORTS
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
	
	private static class Expectation {
		private Mode mode;
		private ISymbol symbol;
		public Expectation(Mode mode, ISymbol symbol) {
			this.mode = mode;
			this.symbol = symbol;
		}
		public ISymbol getSymbol() {
			return symbol;
		}
		public Mode getMode() {
			return mode;
		}
	}
	
	@Parameters(name = "{index}: {0}")
    public static Iterable<File> data() {
    	File rootFolder = new File("examples/analyze-interface");
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
		ISymbolFactory symbolFactory = new SymbolFactory();
		StringBuilder inputBuilder = new StringBuilder();
		List<Expectation> expectations = new LinkedList<Expectation>();
		readTestCase(symbolFactory, inputBuilder, expectations);
		
		//parse input
		IParser parser = new Parser();
		IAnalyzer analyzer = new Analyzer(parser, symbolFactory);
		IPreprocessedSource source = new Source(inputBuilder.toString());
		ISourceInterface iface = analyzer.analyzeSource(source);
		
		//compare with expectations
		for(Expectation exp: expectations) {
			Iterable<ISymbol> symbols = exp.getMode() == Mode.IMPORTS
				? iface.getImports()
				: iface.getExports();
			Iterator<ISymbol> iterator = symbols.iterator();
			boolean found = false;
			while(!found && iterator.hasNext()) {
				ISymbol current = iterator.next();
				found = current.equals(exp.getSymbol());
			}
			if(!found)
				fail("Expected, but not found: "+exp.getMode().name()+" "+exp.getSymbol().getName()+": "+exp.getSymbol().getSymbolType().getName());
		}
	}

	private void readTestCase(ISymbolFactory symbolFactory, StringBuilder inputBuilder, List<Expectation> expectations) throws IOException, TestCaseSyntaxException {
		final String patternInput = "^--input$";
		final String patternOutput = "^--output$";
		final String patternSymbol = "^(imports|exports) ([a-zA-Z\\._0-9]+):([a-zA-Z_0-9]+)$";
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
						Mode mode = strMode.equalsIgnoreCase("imports") ? Mode.IMPORTS : Mode.EXPORTS;
						ISymbolType type = symbolFactory.createSymbolType(strType);
						ISymbol symbol = symbolFactory.createSymbol(strName, type);
						Expectation expectation = new Expectation(mode, symbol);
						expectations.add(expectation);
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