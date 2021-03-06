# import "table"
# import "../packages/function_is_package"
# import "../packages/function_package_contains_name"

CREATE FUNCTION create_classifier (
		given_name VARCHAR(50),
		given_type ENUM('CLASS', 'DATATYPE'),
		given_parent_package_id INT)
	RETURNS INT
	COMMENT 'creates a new classifier by name and parent package, returns id on success or -1 on error.'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE my_id INT;

	-- is parent a package?
	IF NOT is_package(given_parent_package_id) THEN
		RETURN -1;
	END IF;
	
	-- is name already assigned?
	IF package_contains_name(given_parent_package_id, given_name) THEN
		RETURN -1;
	END IF;
	
	-- create named element
	SET my_id = create_named_element(given_name, given_type);

	-- then the classifier
	INSERT INTO classifiers (id, parent_package_id) VALUES(my_id, given_parent_package_id);
	RETURN my_id;
END;