package de.loteslab.mmm.mysqllang;

public enum Expectation {
	NEEDED, //was used at some point in time
	CREATED, //at the position of execution the given symbol is existing
	DROPPED //at the position of execution the given symbol is NOT existing
}
