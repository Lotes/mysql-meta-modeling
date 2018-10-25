package de.loteslab.mmm.sqlc;

@SuppressWarnings("serial")
public class CycleFoundExcpetion extends Exception {
	public CycleFoundExcpetion(Iterable<String> cycle) {
		super("Cycle found: "+String.join(" -> ", cycle));
	}
}
