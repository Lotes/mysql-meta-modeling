﻿#import "table.sql"

CREATE FUNCTION package_contains_name (
		parentID INT,
		name VARCHAR(50)
	)
	RETURNS BOOL
	COMMENT 'checks if name is assigned to a sub package'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE packageId INT DEFAULT -1;
	SELECT p.id FROM view_packages p WHERE p.parent_package_id = parentID INTO packageId ;
	RETURN packageId <> -1;
END;