#SQL

SQLC is a command-line tool that scans SQL files for dependencies like tables, triggers, user, etc.  and executes those files in an order such that all dependecies can be meeted.

##Dialects

* [ ] MySQL (in progress)
* [ ] SQLite (wanted?)
* [ ] T-SQL (wanted?)

##Features

* [ ] [Configuration](features/MACROS.md#definition)
* [ ] [Queries](features/MACROS.md#query)
* [ ] [Branching](features/MACROS.md#branching)
* [ ] [Looping over lists](features/MACROS.md#looping)
* [ ] [Lambdas](features/LAMBDAS.md)
* [ ] dependency visualization
* [ ] result file output
* [ ] logging by directive
* [ ] verbose mode
* [x] manual import with directives
* [ ] automatical topological script execution
* [x] topological script execution by declaration

#License

[MIT](../LICENSE.md) by [Markus Rudolph](https://github.com/Lotes)
