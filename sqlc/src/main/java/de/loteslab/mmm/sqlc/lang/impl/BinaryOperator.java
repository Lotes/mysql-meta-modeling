package de.loteslab.mmm.sqlc.lang.impl;

public enum BinaryOperator {
	ADD, //object, array, null, bool, num, str
	SUBTRACT, MULTIPLY, DIVIDE, MODULO, //num
	GT, GE, LT, LE, //bool, num, str
	EQ, NEQ, //object, array, null, bool, num, str
	SHR, SHL, //number
	XOR, BAND, BOR, //bool+number
	LAND, LOR; //bool
}
