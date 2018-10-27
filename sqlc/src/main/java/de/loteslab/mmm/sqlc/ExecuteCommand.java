package de.loteslab.mmm.sqlc;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.traverse.TopologicalOrderIterator;

import de.loteslab.mmm.sqlc.lang.Script;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
		    try(Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d?user=%s&password=%s&allowMultiQueries=true", 
		    		host, port, userName, password)))
		    {
		    	System.out.println("ok");
		    	
		    	try(Statement statement = conn.createStatement())
		    	{
		    		switch(mode) {
			    	case DROP_AND_CREATE:
			    		statement.addBatch("DROP DATABASE IF EXISTS `"+databaseName+"`");
			    		statement.addBatch("CREATE DATABASE `"+databaseName+"`");
			    		
			    	case USE:
			    		statement.addBatch("USE `"+databaseName+"`");
			    		break;
			    	}
		    		statement.executeBatch();
		    	}
		    	
			    readEntryFiles(conn, file);
		    }
		} catch(CycleFoundExcpetion e) {
			System.err.println("cycle found");
			System.err.println(e.getMessage());
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
	
	private static final Pattern patternImport = Pattern.compile("^#\\s*import\\s+\"([^\"]+)\"$");
	
	private void readEntryFiles(Connection conn, File file) throws IOException, CycleFoundExcpetion {
		HashMap<String, Script> filesContent = new HashMap<String, Script>();
		HashSet<String> filesSet = new HashSet<String>();
		final Graph<String, DefaultEdge> dependencies = new DefaultDirectedGraph<>(DefaultEdge.class);
		Stack<String> filesStack = new Stack<String>();
		
		loadFile(file, filesSet, dependencies, filesStack, filesContent);
		
		//find a cycle
		CycleDetector<String, DefaultEdge> detector = new CycleDetector<>(dependencies);
		if(detector.detectCycles()) {
			throw new CycleFoundExcpetion(detector.findCycles());
		}
		LinkedList<String> filesOrder = new LinkedList<>();
		TopologicalOrderIterator<String, DefaultEdge> iterator = new TopologicalOrderIterator<>(dependencies);
		while(iterator.hasNext())
			filesOrder.add(iterator.next());
		
		for(String found: filesOrder) {
			String query = filesContent.get(found).getContent();
			if(query != null && !query.isEmpty())
			try {
				System.out.print("- executing '"+found+"'...");
				try(Statement statement = conn.createStatement())
				{
					statement.execute(query);	
				}
				System.out.println("ok");
			} catch (SQLException e) {
				System.err.println("failed");
				System.err.println("ERROR at "+found);
				System.err.println(e.getMessage());
			}
		}
	}

	private void loadFile(File file, HashSet<String> filesSet, Graph<String, DefaultEdge> dependencies, Stack<String> filesStack, HashMap<String, Script> filesContent) 
			throws IOException, CycleFoundExcpetion, SecurityException {
		if(!file.isFile())
			return;
		String path = file.getCanonicalFile().getAbsolutePath();
	    if(filesSet.contains(path))
	    	return;
	    if(filesStack.contains(path))
	    	throw new CycleFoundExcpetion(filesStack);
	    filesStack.push(path);
	    filesSet.add(path);
	    dependencies.addVertex(file.getCanonicalFile().getAbsolutePath());
	
	    Charset charset = Charset.forName("ISO-8859-1");
	    LinkedList<File> importedFiles = new LinkedList<File>();
	    StringBuilder builder = new StringBuilder();
	    Files.lines(file.toPath(), charset).forEach(line -> {
	    	Matcher matcher = patternImport.matcher(line);
	        if(matcher.find()) {    
	        	File importFile = new File(file.getParentFile(), matcher.group(1).replaceAll("(\\\\|/)", File.separator)+".sql");
	        	importedFiles.add(importFile);
	        	String from;
				try {
					from = importFile.getCanonicalFile().getAbsolutePath();
				} catch (IOException e) {
					from = importFile.getAbsolutePath();
				}
	        	dependencies.addVertex(from);
	        	dependencies.addEdge(from, path);
	        } else {
	        	builder.append(line);
	        	builder.append("\r\n");
	        }
	    });
	    String content = builder.toString();
	    Script script = new Script(file, content);
	    filesContent.put(file.getAbsolutePath(), script);
	    
	    for(File importFile: importedFiles)
	    	loadFile(importFile, filesSet, dependencies, filesStack, filesContent);
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
