#import "function_class_get_all_sub_types"

CREATE FUNCTION class_is_sub_class_of (
		given_class_id INT,
		given_super_class_id INT
	)
	RETURNS BOOL
	COMMENT 'returns true iff class is sub class of given super class'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE loaded BOOL DEFAULT FALSE;
	DECLARE found BOOL DEFAULT FALSE;
	IF given_class_id = given_super_class_id THEN
		RETURN TRUE;
	END IF;
    CALL class_get_all_sub_types(given_super_class_id);
	SELECT TRUE FROM temp_sub_types WHERE id=given_class_id INTO found;
	RETURN loaded AND found;
END;