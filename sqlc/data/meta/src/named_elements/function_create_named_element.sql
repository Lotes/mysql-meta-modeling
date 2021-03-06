#import "table"

CREATE FUNCTION create_named_element (
		given_name VARCHAR(50),
		given_type ENUM('PACKAGE', 'CLASS', 'DATATYPE', 'REFERENCE', 'ATTRIBUTE')
	)
	RETURNS INT
	COMMENT 'creates a new named element'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	INSERT INTO named_elements (name, type) VALUE (given_name, given_type);
	RETURN LAST_INSERT_ID();
END;