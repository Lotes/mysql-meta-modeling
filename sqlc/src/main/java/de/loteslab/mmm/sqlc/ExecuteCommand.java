package de.loteslab.mmm.sqlc;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.mysql.cj.MysqlConnection;

import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

	@Parameters(paramLabel = "FILE", description = "one ore more files to call")
	private File[] files;
	
	@Option(names = { "--help" }, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;
	
	public void execute() {
		if(databaseName == null)
		{
			System.err.println("Please give me a database name!");
			return;
		}
		
		if(files == null || files.length == 0) {
			System.err.println("No files given!");
			return;
		}
		
		Connection conn = null;
		try {
			String password = this.password == null ? readPwd() : this.password;
			System.out.print(String.format("Connecting to '%s' at port %d as user '%s'...", host, port, userName));
		    conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d?user=%s&password=%s", 
		    		host, port, userName, password));
		    System.out.println("ok");
		    try {
		    	
		    } finally {
		    	conn.close();
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
