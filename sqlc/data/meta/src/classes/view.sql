#import "..\typed_elements\table"
#import "..\named_elements\table"
#import "..\structural_features\table"
#import "table"

CREATE VIEW view_classes AS
SELECT 
	ne.id,
	ne.name,
	cs.abstraction,
	c.parent_package_id
FROM
	named_elements ne,
	classifiers c,
	classes cs
WHERE
	ne.id = c.id 
	AND c.id = cs.id
;