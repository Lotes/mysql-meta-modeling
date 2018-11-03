#Feature: Macros

Define template variables with the power of JSON.

##Definition

```sql
-- define template variables of different types
#define type "VARCHAR(50)"
#define isAbstract false
#define list [1, 2, 3]
#define column { name: "category", color: "#999" }

-- load from sources
#define xxx from file (name+".json")
```

##Query

```sql
--simple
CREATE TABLE friends (
  NAME {{type}} NOT NULL
)

--complex
SELECT * FROM {{table[0].name+"backup"}}
```

##Branching

```sql
{if condition}
SELECT ...
{elseif condition}
SELECT ...
{else}
#error "HELP!"
{/if}
```

##Looping


```sql
{foreach (key,value) in query}
...
{else}
...
{/foreach}
```
