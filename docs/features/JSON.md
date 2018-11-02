Feature: JSON
=============

Define template variables with the power of JSON.

Examples
--------

```sql
-- define template variables of different types
#define type "VARCHAR(50)"
#define isAbstract false
#define list [1, 2, 3]
#define column { name: "category", color: "#999" }

-- load from sources
#define xxx from file (name+".json")

CREATE TABLE friends (
  NAME {{type}} NOT NULL
)
```
