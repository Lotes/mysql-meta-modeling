package de.loteslab.mmm.mysqllang.impl;

import java.util.HashMap;
import java.util.function.Function;

import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolNameNormalizer;
import de.loteslab.mmm.mysqllang.ISymbolType;

public class SymbolFactory implements ISymbolFactory, ISymbolNameNormalizer {
	public static final class TypeNames {
		public static final String TABLE_LIKE = "TABLE_LIKE";
		public static final String DATABASE = "DATABASE";
		public static final String EVENT = "EVENT";
		public static final String USER = "USER";
		public static final String INDEX = "INDEX";
		public static final String LOGFILE_GROUP = "LOGFILE_GROUP";
		public static final String PROCEDURE = "PROCEDURE";
		public static final String FUNCTION = "FUNCTION";
		public static final String SERVER = "SERVER";
		public static final String TABLESPACE = "TABLESPACE";
		public static final String TRIGGER = "TRIGGER";
	}
	
	private static final String PREFIX_CURRENT_DB = "<current_db>.";

	private HashMap<String, ISymbolType> types = new HashMap<String, ISymbolType>();
	private HashMap<ISymbolType, HashMap<String, ISymbol>> symbols = new HashMap<ISymbolType, HashMap<String, ISymbol>>();
	
	private HashMap<ISymbolType, Function<String, String>> normalizers;
	private ISymbolType symbolTypeTableLike;
	private ISymbolType symbolTypeDatabase;
	private ISymbolType symbolTypeUser;
	private ISymbolType symbolTypeEvent;
	private ISymbolType symbolTypeIndex;
	private ISymbolType symbolTypeLogFileGroup;
	private ISymbolType symbolTypeProcedure;
	private ISymbolType symbolTypeFunction;
	private ISymbolType symbolTypeServer;
	private ISymbolType symbolTypeTablespace;
	private ISymbolType symbolTypeTrigger;

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
		symbolTypeTableLike = createSymbolType(TypeNames.TABLE_LIKE);
		symbolTypeDatabase = createSymbolType(TypeNames.DATABASE);
		symbolTypeUser = createSymbolType(TypeNames.USER);
		symbolTypeEvent = createSymbolType(TypeNames.EVENT);
		symbolTypeIndex = createSymbolType(TypeNames.INDEX);
		symbolTypeLogFileGroup = createSymbolType(TypeNames.LOGFILE_GROUP);
		symbolTypeProcedure = createSymbolType(TypeNames.PROCEDURE);
		symbolTypeFunction = createSymbolType(TypeNames.FUNCTION);
		symbolTypeServer = createSymbolType(TypeNames.SERVER);
		symbolTypeTablespace = createSymbolType(TypeNames.TABLESPACE);
		symbolTypeTrigger = createSymbolType(TypeNames.TRIGGER);
		
		normalizers = new HashMap<ISymbolType, Function<String, String>>();
		normalizers.put(symbolTypeTableLike, str -> normalizeTableName(str));
		normalizers.put(symbolTypeDatabase, str -> normalizeDatabaseName(str));
		normalizers.put(symbolTypeUser, str -> normalizeUser(str));
		normalizers.put(symbolTypeEvent, str -> normalizeEvent(str));
		normalizers.put(symbolTypeIndex, str -> normalizeIndex(str));
		normalizers.put(symbolTypeLogFileGroup, str -> normalizeLogFileGroup(str));
		normalizers.put(symbolTypeProcedure, str -> normalizeFullId(str));
		normalizers.put(symbolTypeFunction, str -> normalizeFullId(str));
		normalizers.put(symbolTypeServer, str -> str.toLowerCase());
		normalizers.put(symbolTypeTablespace, str -> str.toLowerCase());
		normalizers.put(symbolTypeTrigger, str -> normalizeFullId(str));
	}
	
	private String normalizeLogFileGroup(String str) {
		return str.toLowerCase();
	}

	private String normalizeIndex(String str) {
		return normalizeFullId(str);
	}

	private String normalizeEvent(String str) {
		return normalizeFullId(str);
	}

	private String normalizeUser(String str) {
		return str;
	}

	private String normalizeDatabaseName(String str) {
		return str.toLowerCase();
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
		return normalizeFullId(text);
	}

	private String normalizeFullId(String text) {
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
