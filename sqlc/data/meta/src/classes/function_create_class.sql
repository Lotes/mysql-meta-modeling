# import "table"
# import "../classifiers/function_create_classifier"

CREATE FUNCTION create_class (
		given_name VARCHAR(50),
		given_abstraction ENUM ('CLASS', 'ABSTRACT', 'INTERFACE'),
		given_parent_package_id INT
	)
	RETURNS INT
	COMMENT 'creates a new class by name, abstraction and parent package, returns id on success or -1 on error.'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE my_id INT;

	SET my_id = create_classifier(given_name, 'CLASS', given_parent_package_id);
	IF my_id <> -1 THEN
		INSERT INTO classes (id, abstraction) VALUES(my_id, given_abstraction);
	END IF;
	
	RETURN my_id;
END;