# import "table"
# import "function_is_package"
# import "function_package_contains_name"

CREATE FUNCTION create_package (
		name VARCHAR(50),
		parent_id INT
	)
	RETURNS INT
	COMMENT 'creates a new package by name and parent package, returns id on success or -1 on error.'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE my_id INT;
	DECLARE my_parent INT;

	IF parent_id <> -1 AND NOT is_package(parent_id) THEN
		RETURN -1;
	END IF;
	IF package_contains_name(parent_id, name) THEN
		RETURN -1;
	END IF;
	
	SET my_id = create_named_element(name, 'PACKAGE');

	IF parent_id <> -1 THEN
		INSERT INTO packages (id, parent_package_id) VALUES(my_id, parent_id);
	ELSE
		INSERT INTO packages (id, parent_package_id) VALUES(my_id, NULL);
	END IF;

	RETURN my_id;
END;