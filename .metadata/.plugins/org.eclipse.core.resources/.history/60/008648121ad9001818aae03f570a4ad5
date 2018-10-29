#import "..\named_elements\table"
#import "table"

CREATE VIEW view_packages AS
SELECT 
	ne.id,
	ne.name,
	p.parent_package_id
FROM
	named_elements ne,
	packages p
WHERE
	ne.id = p.id
;
