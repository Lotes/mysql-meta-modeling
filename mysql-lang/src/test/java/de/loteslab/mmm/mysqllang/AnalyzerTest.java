package de.loteslab.mmm.mysqllang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

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
	private enum State {
		EMPTY, INPUT, OUTPUT
	}
	
	private enum Mode {
		IMPORTS, EXPORTS
	}
	
	private class Expectation {
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
	public void test() throws FileNotFoundException, IOException {
		final String patternInput = "^--input$";
		final String patternOutput = "^--output$";
		final String patternSymbol = "^(imports|exports) ([a-zA-Z\\._0-9]+):([a-zA-Z_0-9]+)$";
		ISymbolFactory symbolFactory = new SymbolFactory();
		IAnalyzer analyzer = new Analyzer(symbolFactory);
		
		State state = State.EMPTY;
		StringBuilder inputBuilder = new StringBuilder();
		LinkedList<Expectation> outputBuilder = new LinkedList<Expectation>();
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
					if(line.matches(patternSymbol)) {
						
					}
					break;
				default:
					break;
				}
			}
		}
	}
}