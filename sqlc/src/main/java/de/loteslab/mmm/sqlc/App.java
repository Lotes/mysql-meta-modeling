package de.loteslab.mmm.sqlc;

import picocli.CommandLine;

public class App 
{
    public static void main( String[] args)
    {
    	ExecuteCommand cmd = new ExecuteCommand();
        CommandLine cli = new CommandLine(cmd);
        cli.parse(args);
        cmd.execute();        
    }
}
