# SQLC

SQLC is a command-line tool that scans SQL files for dependencies like tables, triggers, user, etc.  and executes those files in an order such that all dependecies can be meeted.

## Dialects

* [ ] MySQL (in progress)
* [ ] maybe SQLite
* [ ] maybe T-SQL
* [ ] maybe PostgreSQL

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
* [ ] automatical topological script execution
* [ ] [topological script execution](features/DEPENDENCIES.md) 
  * [x] by declaration
  * [ ] by parsing

# License

[MIT](../LICENSE.md) by [Markus Rudolph](https://github.com/Lotes)
