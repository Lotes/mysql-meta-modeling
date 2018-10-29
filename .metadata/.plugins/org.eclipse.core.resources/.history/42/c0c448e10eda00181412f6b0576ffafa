# import "..\classifiers\table"

CREATE TABLE classes (
	id INT NOT NULL,
	is_abstract BOOL NOT NULL DEFAULT FALSE,
	is_interface BOOL NOT NULL DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES classifiers(id)
		ON DELETE CASCADE
);