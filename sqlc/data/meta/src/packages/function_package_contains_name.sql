# import "table"

CREATE FUNCTION package_contains_name (
		given_parent_id INT,
		given_name VARCHAR(50)
	)
	RETURNS BOOL
	COMMENT 'checks if name is assigned to a sub package'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE package_id INT DEFAULT -1;
	SELECT p.id 
	FROM view_packages p JOIN named_elements ne USING(id) 
	WHERE p.parent_package_id = given_parent_id AND ne.name = given_name
	INTO package_id;
	RETURN package_id <> -1;
END;