#import "table.sql"

CREATE FUNCTION is_package (
		given_id INT
	)
	RETURNS BOOL
	COMMENT 'checks if id is a valid package'
	LANGUAGE SQL
	NOT DETERMINISTIC
	READS SQL DATA
	SQL SECURITY DEFINER
BEGIN
	DECLARE packageId INT DEFAULT -1;
	SELECT id FROM view_packages vp WHERE vp.id=given_id INTO packageId;
	RETURN packageId <> -1;
END;