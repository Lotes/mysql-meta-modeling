﻿CREATE PROCEDURE assert_is_true (
		value BOOL,
		txt VARCHAR(100)
	)
	COMMENT 'signals an error if value is not false'
	LANGUAGE SQL
	DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	IF NOT value THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = txt;
	END IF;
END;