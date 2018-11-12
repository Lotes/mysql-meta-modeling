# SQLC

Goal:

_SQLC is a command-line tool that scans SQL files for dependencies like tables, triggers, user, etc.  and executes those files in an order such that all dependecies can be meet. SQLC is also a JSON-driven preprocessor and template engine. The idea is to minimize SQL code duplicates: just define some metadata to describe your database system. That is the input for creating the queries that constructs your system._

## Examples

TODO

## Dialects

Planned:

* [ ] MySQL (in progress)

Open for extension:

* [ ] SQLite
* [ ] T-SQL
* [ ] PostgreSQL

## Features

* [ ] [Configuration](features/MACROS.md#definition)
* [ ] [Queries](features/MACROS.md#query)
* [ ] [Branching](features/MACROS.md#branching)
* [ ] [Looping over lists](features/MACROS.md#looping)
* [ ] [Lambdas](features/LAMBDAS.md)
* [ ] [dependency visualization](features/DEPENDENCIES.md)
* [ ] result file output
* [ ] logging and error handling by directive
* [ ] verbose mode
* [ ] [topological script execution](features/DEPENDENCIES.md) 
  * [x] by declaration
  * [ ] by parsing (in progress)

# License

[MIT](../LICENSE.md) by [Markus Rudolph](https://github.com/Lotes)
