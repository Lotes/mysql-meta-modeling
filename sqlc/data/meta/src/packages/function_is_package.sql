# import "table"

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
	DECLARE packageId INT DEFAULT -1;
	
	SELECT id
	FROM named_elements ne
	WHERE ne.id=given_id AND ne.type='PACKAGE'
	INTO packageId;
	
	RETURN packageId <> -1;
END;