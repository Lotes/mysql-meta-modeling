#import "function_is_class"
#import "table_super_types"
#import "table"

CREATE PROCEDURE class_get_all_super_types (
		given_class_id INT
	)
	COMMENT 'returns all super types in a temporary table `temp_super_types`'
	LANGUAGE SQL
	NOT DETERMINISTIC
	MODIFIES SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE todo_count INT;
	DECLARE next_id INT;
	
	CREATE TEMPORARY TABLE IF NOT EXISTS temp_super_types_todo (id INT PRIMARY KEY);
	CREATE TEMPORARY TABLE IF NOT EXISTS temp_super_types (id INT PRIMARY KEY);
	DELETE FROM temp_super_types_todo;
	DELETE FROM temp_super_types;

	INSERT INTO temp_super_types_todo (id) VALUES (given_class_id);
	
	REPEAT
		SELECT id FROM temp_super_types_todo LIMIT 1 INTO next_id;
		DELETE FROM temp_super_types_todo WHERE id = next_id;
	
		INSERT IGNORE INTO temp_super_types (id)
		SELECT super_type_id FROM class_super_types WHERE id = next_id;
		
		SELECT COUNT(*) FROM temp_super_types_todo INTO todo_count;
	UNTIL todo_count = 0 
	END REPEAT;
END;