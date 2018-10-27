#import "function_is_class"
#import "table_supertypes"

CREATE FUNCTION class_add_super_type (
		given_class_id INT,
		given_super_type_id INT
	)
	RETURNS BOOL
	COMMENT 'adds a new supertype to a class, returns true on success and false on error'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	-- TODO given_class_id Not class -> error
	-- TODO given_super_type_id Not class -> error
	-- TODO isSubCLassOf(given_class_id, given_super_type_id) -> false
	-- TODO isSubCLassOf(given_super_type_id, given_class_id) -> false
	-- TODO check count of abstracts, classes and interfaces in inheriance tree
	-- TODO create link

	RETURN TRUE;
END;