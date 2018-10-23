#import "..\structural_features\table.sql"

CREATE TABLE attributes (
	id INT NOT NULL,
	is_id BOOL NOT NULL DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES structural_features(id)
		ON DELETE CASCADE
);