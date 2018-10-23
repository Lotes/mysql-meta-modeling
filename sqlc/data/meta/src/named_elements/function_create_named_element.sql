﻿#import "table.sql"

CREATE FUNCTION create_named_element (
		given_name VARCHAR(50)
	)
	RETURNS INT
	COMMENT 'creates a new named element'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	INSERT INTO named_elements (name) VALUE (given_name);
	RETURN LAST_INSERT_ID();
END;