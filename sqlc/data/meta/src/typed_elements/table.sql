#import "..\named_elements\table.sql"
#import "..\classifiers\table.sql"

CREATE TABLE typed_elements (
	id INT NOT NULL,
	type_id INT NOT NULL,
	lower_bound INT NOT NULL DEFAULT 0,
	upper_bound INT NOT NULL DEFAULT 1,
	is_ordered BOOL NOT NULL DEFAULT TRUE,
	is_unique BOOL NOT NULL DEFAULT TRUE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES named_elements(id)
		ON DELETE CASCADE,
	FOREIGN KEY(type_id) REFERENCES typed_elements(id)
		ON DELETE CASCADE
);