#import "..\named_elements\table"

CREATE TABLE packages (
	id INT NOT NULL,
	parent_package_id INT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(id) REFERENCES named_elements(id),
	FOREIGN KEY(parent_package_id) REFERENCES packages(id) 
		ON DELETE CASCADE
);