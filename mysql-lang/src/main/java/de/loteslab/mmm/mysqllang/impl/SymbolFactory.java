package de.loteslab.mmm.mysqllang.impl;

import java.util.HashMap;
import java.util.function.Function;

import de.loteslab.mmm.mysqllang.ISourceExecution;
import de.loteslab.mmm.mysqllang.ISymbol;
import de.loteslab.mmm.mysqllang.ISymbolFactory;
import de.loteslab.mmm.mysqllang.ISymbolNameNormalizer;
import de.loteslab.mmm.mysqllang.ISymbolType;

public class SymbolFactory implements ISymbolFactory, ISymbolNameNormalizer {
	private static final class TypeNames {
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
	
	public class Types {
		private HashMap<ISymbolType, Function<String, String>> normalizers;
		
		public Types() {
			TableLike =  createSymbolType(SymbolFactory.TypeNames.TABLE_LIKE);
			Database = createSymbolType(SymbolFactory.TypeNames.DATABASE);
			User = createSymbolType(SymbolFactory.TypeNames.USER);
			Event = createSymbolType(SymbolFactory.TypeNames.EVENT);
			Index = createSymbolType(SymbolFactory.TypeNames.INDEX);
			LogFileGroup = createSymbolType(SymbolFactory.TypeNames.LOGFILE_GROUP);
			Procedure = createSymbolType(SymbolFactory.TypeNames.PROCEDURE);
			Function = createSymbolType(SymbolFactory.TypeNames.FUNCTION);
			Server = createSymbolType(SymbolFactory.TypeNames.SERVER);
			Tablespace = createSymbolType(SymbolFactory.TypeNames.TABLESPACE);
			Trigger = createSymbolType(SymbolFactory.TypeNames.TRIGGER);
			
			normalizers = new HashMap<ISymbolType, Function<String, String>>();
			normalizers.put(TableLike, str -> normalizeFullId(str));
			normalizers.put(Database, str -> str.toLowerCase());
			normalizers.put(User, str -> str);
			normalizers.put(Event, str -> normalizeFullId(str));
			normalizers.put(Index, str -> normalizeFullId(str));
			normalizers.put(LogFileGroup, str -> str.toLowerCase());
			normalizers.put(Procedure, str -> normalizeFullId(str));
			normalizers.put(Function, str -> normalizeFullId(str));
			normalizers.put(Server, str -> str.toLowerCase());
			normalizers.put(Tablespace, str -> str.toLowerCase());
			normalizers.put(Trigger, str -> normalizeFullId(str));
		}
		
		public final ISymbolType TableLike;
		public final ISymbolType Database;
		public final ISymbolType User;
		public final ISymbolType Event;
		public final ISymbolType Index;
		public final ISymbolType LogFileGroup;
		public final ISymbolType Procedure;
		public final ISymbolType Function;
		public final ISymbolType Server;
		public final ISymbolType Tablespace;
		public final ISymbolType Trigger;
		
		public String normalizeName(ISymbolType type, String name) {
			if(normalizers.containsKey(type))
				return normalizers.get(type).apply(name);
			return name;
		}
	}
	
	public static final String CURRENT_DB = "<current_db>";
	private static final String PREFIX_CURRENT_DB = CURRENT_DB+".";

	private HashMap<String, ISymbolType> types = new HashMap<String, ISymbolType>();
	private HashMap<ISymbolType, HashMap<String, ISymbol>> symbols = new HashMap<ISymbolType, HashMap<String, ISymbol>>();

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
		private ISourceExecution execution = null;
		private String name;
		private ISymbolType type;
		public Symbol(String name, ISymbolType type, ISourceExecution execution) {
			this.name = SymbolFactory.this.normalize(type, name);
			this.type = type;
			this.execution = execution;
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

		@Override
		public ISourceExecution getExecution() {
			return execution;
		}
	}
	
	public final Types Predefined = this.new Types();
	
	public SymbolFactory() {
		
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
	public ISymbol createSymbol(String name, ISymbolType type, ISourceExecution execution) {
		if(!symbols.containsKey(type))
			symbols.put(type, new HashMap<String, ISymbol>());
		HashMap<String, ISymbol> map = symbols.get(type);
		String key = Predefined.normalizeName(type, name);
		if(!map.containsKey(key)) {
			map.put(key, new Symbol(key, type, execution));
		}
		return map.get(key);
	}


	private String normalizeFullId(String text) {
		if(!text.contains("."))
			text = PREFIX_CURRENT_DB+text;
		return text.toLowerCase();
	}

	@Override
	public String normalize(ISymbolType type, String name) {
		return Predefined.normalizeName(type, name);
	}
}
