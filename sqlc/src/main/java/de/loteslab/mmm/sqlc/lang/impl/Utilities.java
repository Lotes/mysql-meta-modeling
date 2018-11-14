package de.loteslab.mmm.sqlc.lang.impl;

import de.loteslab.mmm.lang.MacroParser.PairNameContext;

public class Utilities {
	public static String extractName(PairNameContext ctx) {
		String value = ctx.getText();
		if(value.startsWith("\""))
			return extractString(value);
		return value;
	}
	
	public static String extractString(String value) {
		StringBuilder builder = new StringBuilder(value.length());
		int index = 1;
		while(index < value.length()-1) {
			char c = value.charAt(index);
			if(c > 0x1F) {
				builder.append(c);
			} else if(c == '\\') {
				c = value.charAt(++index);
				switch(c) {
				case '"': builder.append('"'); break;
				case '\\': builder.append('\\'); break;
				case '/': builder.append('/'); break;
				case 'b': builder.append('\b'); break;
				case 'f': builder.append('\f'); break;
				case 'n': builder.append('\n'); break;
				case 'r': builder.append('\r'); break;
				case 'u':
					String hex = ""+value.charAt(++index)
						+ value.charAt(++index)
						+ value.charAt(++index)
						+ value.charAt(++index);
					c = (char)Integer.parseInt(hex, 16);
					builder.append(c);
					break;
				}
			}
			index++;
		}
		return builder.toString();
	}

}
