﻿#import "..\typed_elements\table.sql"
#import "..\named_elements\table.sql"
#import "..\structural_features\table.sql"
#import "table.sql"

CREATE VIEW view_datatypes AS
SELECT 
	ne.id,
	ne.name,
	te.type_id,
	te.lower_bound,
	te.upper_bound,
	te.is_ordered,
	te.is_unique,
	r.is_id
FROM
	named_elements ne,
	typed_elements te,
	structural_features sf,
	attributes r
WHERE
	ne.id = te.id 
	AND te.id = sf.id
	AND sf.id = r.id
;