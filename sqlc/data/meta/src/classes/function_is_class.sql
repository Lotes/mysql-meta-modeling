#import "table"

CREATE FUNCTION is_class (
		given_id INT
	)
	RETURNS BOOL
	COMMENT 'checks if id is a valid class'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE class_id INT DEFAULT -1;
	
	SELECT id
	FROM named_elements ne
	WHERE ne.id=given_id AND ne.type='CLASS'
	INTO class_id;
	
	RETURN class_id <> -1;
END;