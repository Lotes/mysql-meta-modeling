#import "..\typed_elements\table"
#import "..\classes\table"

CREATE TABLE structural_features (
	id INT NOT NULL,
	class_id INT NOT NULL,
	is_changeable BOOL NOT NULL DEFAULT TRUE,
	is_volatile BOOL NOT NULL DEFAULT FALSE,
	is_derived BOOL NOT NULL DEFAULT FALSE,
	is_transient BOOL NOT NULL DEFAULT FALSE,
	is_unsettable BOOL NOT NULL DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES typed_elements(id)
		ON DELETE CASCADE,
	FOREIGN KEY(class_id) REFERENCES classes(id)
		ON DELETE CASCADE
);