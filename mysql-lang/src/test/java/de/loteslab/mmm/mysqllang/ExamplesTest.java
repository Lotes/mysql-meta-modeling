package de.loteslab.mmm.mysqllang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.loteslab.mmm.mysqllang.impl.Parser;

@RunWith(Parameterized.class)
public class ExamplesTest {
	@Parameters(name = "{index}: {0}")
    public static Iterable<File> data() {
    	File rootFolder = new File("examples/parse-only");
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
		IParser parser = new Parser();
		try(FileInputStream stream = new FileInputStream(file)) {
			parser.parse(stream);
		}
	}
}
