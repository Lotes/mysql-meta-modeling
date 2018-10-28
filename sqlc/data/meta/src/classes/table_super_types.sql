# import "table"

CREATE TABLE class_super_types (
	id INT NOT NULL,
	super_type_id INT NOT NULL,
	PRIMARY KEY(id, super_type_id),
	FOREIGN KEY(id) REFERENCES classes(id)
		ON DELETE CASCADE,
	FOREIGN KEY(super_type_id) REFERENCES classes(id)
		ON DELETE CASCADE
);