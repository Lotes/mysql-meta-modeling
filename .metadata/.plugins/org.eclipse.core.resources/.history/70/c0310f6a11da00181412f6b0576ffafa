# import "..\typed_elements\table"
# import "..\named_elements\table"
# import "..\structural_features\table"
# import "table"

CREATE VIEW view_classes AS
SELECT 
	ne.id,
	ne.name,
	te.type_id,
	te.lower_bound,
	te.upper_bound,
	te.is_ordered,
	te.is_unique,
	r.is_abstract,
	r.is_interface
FROM
	named_elements ne,
	typed_elements te,
	structural_features sf,
	classes r
WHERE
	ne.id = te.id 
	AND te.id = sf.id
	AND sf.id = r.id
;