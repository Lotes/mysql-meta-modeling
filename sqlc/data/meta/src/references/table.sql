#import "..\structural_features\table"

CREATE TABLE refs (
	id INT NOT NULL,
	is_containment BOOL NOT NULL DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES structural_features(id)
		ON DELETE CASCADE
);