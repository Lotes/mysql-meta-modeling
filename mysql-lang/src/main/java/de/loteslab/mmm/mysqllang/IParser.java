package de.loteslab.mmm.mysqllang;

import java.io.IOException;
import java.io.InputStream;

import de.loteslab.mmm.mysqllang.internal.MySqlParser.RootContext;

public interface IParser {
	RootContext parse(InputStream stream) throws IOException;
}
