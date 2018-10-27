# import "table"

CREATE FUNCTION is_classifier (
		given_id INT
	)
	RETURNS BOOL
	COMMENT 'checks if id is a valid classifier'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE classifierId INT DEFAULT -1;
	
	SELECT id
	FROM named_elements ne
	WHERE ne.id=given_id AND (ne.type='CLASS' OR ne.type='DATATYPE')
	INTO classifierId;
	
	RETURN classifierId <> -1;
END;