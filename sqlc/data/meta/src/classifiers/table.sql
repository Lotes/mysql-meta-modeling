# import "..\packages\table"
# import "..\named_elements\table"

CREATE TABLE classifiers (
	id INT NOT NULL,
	parent_package_id INT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(parent_package_id) REFERENCES packages(id) 
		ON DELETE CASCADE,
	FOREIGN KEY(id) REFERENCES named_elements(id) 
		ON DELETE CASCADE
);