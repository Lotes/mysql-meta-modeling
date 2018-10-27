#import "table"

CREATE FUNCTION is_package (
		given_id INT
	)
	RETURNS BOOL
	COMMENT 'checks if id is a valid package'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE package_id INT DEFAULT -1;
	
	SELECT ne.id
	FROM named_elements ne
	WHERE ne.id=given_id AND ne.type='PACKAGE'
	INTO package_id;
	
	RETURN package_id <> -1;
END;