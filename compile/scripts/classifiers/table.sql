#import "..\packages\table.sql"
#import "..\named_elements\table.sql"

CREATE TABLE classifiers (
	id INT NOT NULL,
	package_id INT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(package_id) REFERENCES packages(id) 
		ON DELETE CASCADE,
	FOREIGN KEY(id) REFERENCES named_elements(id) 
		ON DELETE CASCADE
);