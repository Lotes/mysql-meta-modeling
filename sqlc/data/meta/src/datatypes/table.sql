# import "..\classifiers\table"

CREATE TABLE datatypes (
	id INT NOT NULL,
	native_m VARCHAR(100) NOT NULL,
	native_csharp VARCHAR(100) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES classifiers(id)
		ON DELETE CASCADE
);