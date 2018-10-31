package de.loteslab.mmm.mysqllang.impl;

import java.util.HashMap;
import java.util.function.Function;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolNameNormalizer;
import de.loteslab.mmm.mysqllang.ISymbolType;

public class SymbolFactory implements ISymbolFactory, ISymbolNameNormalizer {
	private static final String PREFIX_CURRENT_DB = "<current_db>.";

	private HashMap<String, ISymbolType> types = new HashMap<String, ISymbolType>();
	private HashMap<ISymbolType, HashMap<String, ISymbol>> symbols = new HashMap<ISymbolType, HashMap<String, ISymbol>>();
	
	private HashMap<ISymbolType, Function<String, String>> normalizers;
	private ISymbolType symbolTypeTableLike;
	
	private static class SymbolType implements ISymbolType {
		private String name;
		
		public SymbolType(String name) {
			this.name = SymbolFactory.normalizeName(name);
		}
		
		@Override
		public String getName() {
			return name;
		}
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ISymbolType))
				return false;
			ISymbolType other = (ISymbolType)obj;
			return other.getName().equals(getName());
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}
	private class Symbol implements ISymbol {
		private String name;
		private ISymbolType type;
		public Symbol(String name, ISymbolType type) {
			this.name = SymbolFactory.this.normalize(type, name);
			this.type = type;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public ISymbolType getSymbolType() {
			return type;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ISymbol))
				return false;
			ISymbol other = (ISymbol)obj;
			return other.getName().equals(name) && other.getSymbolType().equals(type);
		}
	}
	
	public SymbolFactory() {
		symbolTypeTableLike = createSymbolType(SymbolNames.TABLE_LIKE);
		
		normalizers = new HashMap<ISymbolType, Function<String, String>>();
		normalizers.put(symbolTypeTableLike, str -> normalizeTableName(str));
	}
	
	public static String normalizeName(String name) {
		return name.toLowerCase();
	}

	@Override
	public ISymbolType createSymbolType(String name) {
		String key = normalizeName(name);
		if(!types.containsKey(key))
			types.put(key, new SymbolType(name));
		return types.get(key);
	}

	@Override
	public ISymbol createSymbol(String name, ISymbolType type) {
		if(!symbols.containsKey(type))
			symbols.put(type, new HashMap<String, ISymbol>());
		HashMap<String, ISymbol> map = symbols.get(type);
		String key = normalizeName(name);
		if(!map.containsKey(key)) {
			map.put(key, new Symbol(key, type));
		}
		return map.get(key);
	}

	private String normalizeTableName(String text) {
		if(!text.contains("."))
			text = PREFIX_CURRENT_DB+text;
		return text.toLowerCase();
	}

	@Override
	public String normalize(ISymbolType type, String name) {
		if(normalizers.containsKey(type))
			return normalizers.get(type).apply(name);
		return null;
	}
}
