# import "..\classifiers\table"

CREATE TABLE classes (
	id INT NOT NULL,
	abstraction ENUM('CLASS', 'ABSTRACT', 'INTERFACE') NOT NULL DEFAULT 'CLASS',
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES classifiers(id)
		ON DELETE CASCADE
);