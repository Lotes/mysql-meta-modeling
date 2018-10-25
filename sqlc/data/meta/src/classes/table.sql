#import "..\classifiers\table"

CREATE TABLE classes (
	id INT NOT NULL,
	is_abstract BOOL NOT NULL DEFAULT FALSE,
	is_interface BOOL NOT NULL DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES classifiers(id)
		ON DELETE CASCADE
);

CREATE TABLE class_super_types (
	id INT NOT NULL,
	super_type_id INT NOT NULL,
	PRIMARY KEY(id, super_type_id),
	FOREIGN KEY(id) REFERENCES classes(id)
		ON DELETE CASCADE,
	FOREIGN KEY(super_type_id) REFERENCES classes(id)
		ON DELETE CASCADE
);