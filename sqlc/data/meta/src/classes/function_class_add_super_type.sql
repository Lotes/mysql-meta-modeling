#import "function_is_class"
#import "table_super_types"
#import "function_class_is_sub_class_of"
#import "table"

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
	DECLARE class_count INT DEFAULT 0;
	
	-- is given_class_id a real class?
	IF NOT is_class(given_class_id) THEN
		RETURN FALSE;
	END IF;
	
	-- is given_super_type_id a real class?
	IF NOT is_class(given_super_type_id) THEN
		RETURN FALSE;
	END IF;
	
	-- is given class already sub class of super type?
	IF class_is_sub_class_of(given_class_id, given_super_type_id) THEN
		RETURN FALSE;
	END IF;
	
	-- is given super type already sub class of given class?
	IF class_is_sub_class_of(given_super_type_id, given_class_id) THEN
		RETURN FALSE;
	END IF;

	-- allow only one class!
	SELECT COUNT(*) INTO class_count
	FROM classes c JOIN class_super_types cs ON c.id=cs.super_type_id
	WHERE cs.id=given_class_id AND c.abstraction <> 'INTERFACE';
	IF class_count >= 1 THEN
		RETURN FALSE;
	END IF;
	
	-- everything is fine, create link!
	INSERT INTO class_super_types (id, super_type_id) VALUES (given_class_id, given_super_type_id);
	RETURN TRUE;
END;