package de.loteslab.mmm.mysqllang.impl;

import java.util.HashSet;

import de.loteslab.mmm.mysqllang.ISourceInterface;
import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.internal.MySqlParserBaseVisitor;

public class SourceInterfaceVisitor extends MySqlParserBaseVisitor<ISourceInterface> {
	private static class SourceInterface implements ISourceInterface {
		public static SourceInterface Combine(Iterable<SourceInterface> partsOfSameSource) {
			SourceInterface result = new SourceInterface();
			for(SourceInterface part: partsOfSameSource) {
				for(ISymbol symbol: part.getImports())
					if(!result.exports.contains(symbol))
						result.addImport(symbol);
				for(ISymbol symbol: part.getExports())
					result.addExport(symbol);
			}
			return result;
		}
		
		private HashSet<ISymbol> imports = new HashSet<ISymbol>();
		private HashSet<ISymbol> exports = new HashSet<ISymbol>();
		
		public void addImport(ISymbol symbol) {
			imports.add(symbol);
		}
		
		public void addExport(ISymbol symbol) {
			exports.add(symbol);
		}
		
		@Override
		public Iterable<ISymbol> getImports() {
			return imports;
		}

		@Override
		public Iterable<ISymbol> getExports() {
			return exports;
		}
	}

	private ISymbolFactory factory;
	
	public SourceInterfaceVisitor(ISymbolFactory factory) {
		this.factory = factory;
	}
}
