--input
CREATE INDEX jobTitle ON employees(jobTitle);
--output
EXPORTS jobTitle:INDEX
IMPORTS employees:TABLE_LIKE