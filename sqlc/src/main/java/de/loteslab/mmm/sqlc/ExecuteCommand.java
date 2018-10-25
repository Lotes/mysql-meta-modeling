package de.loteslab.mmm.sqlc;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.mysql.cj.MysqlConnection;

import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecuteCommand {
	@Option(names = {"-h", "--host"}, defaultValue = "localhost", type = String.class, arity = "1..1", description = "database host", showDefaultValue=Visibility.ALWAYS)
	private String host;
	
	@Option(names = {"-P", "--port"}, defaultValue = "3306", type = int.class, arity="1..1", description="database port", showDefaultValue=Visibility.ALWAYS)
	private int port;
	
	@Option(names = {"-p", "--password"}, description="database passphrase", hidden=true)
	private String password;
	
	@Option(names = {"-u", "--username"}, defaultValue = "root", type=String.class, arity = "1..1", description="database username", showDefaultValue=Visibility.ALWAYS)
	private String userName;
	
	@Option(names = {"-d", "--database"}, type=String.class, arity = "1..1", description="database name")
	private String databaseName;
	
	@Option(names = {"-m", "--mode"}, type=Mode.class, defaultValue="DROP_AND_CREATE", showDefaultValue=Visibility.ALWAYS, description="database subscription mode", arity = "1..1")
	private Mode mode;

	@Parameters(paramLabel = "FILE", description = "one file to call", arity="1..1")
	private File file;
	
	@Option(names = { "--help" }, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;
	
	public void execute() {
		if(databaseName == null) {
			System.err.println("Please give me a database name!");
			return;
		}
		
		if(file == null) {
			System.err.println("No files given!");
			return;
		}
		
		try {
			String password = this.password == null ? readPwd() : this.password;
			System.out.print(String.format("Connecting to '%s' at port %d as user '%s'...", host, port, userName));
		    try(Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d?user=%s&password=%s", 
		    		host, port, userName, password)))
		    {
		    	System.out.println("ok");
		    	
		    	try(Statement statement = conn.createStatement())
		    	{
		    		switch(mode) {
			    	case DROP_AND_CREATE:
			    		statement.addBatch("DROP DATABASE IF EXISTS `"+databaseName+"`;");
			    		statement.addBatch("CREATE DATABASE `"+databaseName+"`;");
			    		
			    	case USE:
			    		statement.addBatch("USE `"+databaseName+"`;");
			    		break;
			    	}
		    		statement.executeBatch();
		    	}
		    	
			    readEntryFiles(conn, file);
		    }
		} catch(IOException ex) {
			System.err.println("failed");
			System.err.println("IO Error: "+ex.getMessage());
		} catch (SQLException ex) {
			System.err.println("failed");
			System.err.println("SQLException: " + ex.getMessage());
		    System.err.println("SQLState: " + ex.getSQLState());
		    System.err.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private static final Pattern patternImport = Pattern.compile("^#import \"([^\"]+)\"$");
	
	private void readEntryFiles(Connection conn, File file) throws IOException {
		HashSet<String> filesSet = new HashSet<String>();
		LinkedList<File> filesOrder = new LinkedList<File>();
		Stack<String> filesStack = new Stack<String>();
		Object context = new Object();
		try {
			context = loadFile(file, filesSet, filesOrder, filesStack, context);
		} catch (CycleFoundExcpetion e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}	
		
		for(File found: filesOrder) {
			String query = readFile(found.getAbsolutePath(), Charset.forName("UTF-8"));
			try {
				try(Statement statement = conn.createStatement())
				{
					statement.execute(query);	
				}
			} catch (SQLException e) {
				System.err.println("ERROR at "+found.getName());
				System.err.println(e.getMessage());
			}
		}
	}

	private Object loadFile(File file, HashSet<String> filesSet, LinkedList<File> filesOrder, Stack<String> filesStack, Object context) throws IOException, CycleFoundExcpetion {
		if(!file.isFile())
			return context;
		String path = file.getAbsolutePath();
	    if(filesSet.contains(path))
	    	return context;
	    if(filesStack.contains(path))
	    	throw new CycleFoundExcpetion(filesStack);
	    filesStack.push(path);
	    filesSet.add(path);
	    filesOrder.addFirst(file);
	    
	    LinkedList<File> importedFiles = new LinkedList<File>();
	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        Matcher matcher = patternImport.matcher(line);
		        if(matcher.find()) {    
		        	File importFile = new File(file.getParentFile(), matcher.group(1)+".sql");
		        	importedFiles.add(importFile);
		        	continue;
		        }
		    }
		}
	    
	    for(File importFile: importedFiles)
	    	loadFile(importFile, filesSet, filesOrder, filesStack, context);
	    
	    return context;
	}
	
	private static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}

	private static String readPwd() throws IOException {
	    Console c=System.console();
	    if (c==null) { //IN ECLIPSE IDE
	        System.out.print("Password: ");
	        InputStream in=System.in;
	        int max=50;
	        byte[] b=new byte[max];

	        int l= in.read(b);
	        l--;//last character is \n
	        if (l>0) {
	            byte[] e=new byte[l];
	            System.arraycopy(b,0, e, 0, l);
	            return new String(e);
	        } else {
	            return null;
	        }
	    } else { //Outside Eclipse IDE
	        return new String(c.readPassword("Password: "));
	    }
	}
}
