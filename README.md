# TableExtractor

This project defines a Groovy-based DSL for extracting data from an Excel sheet with minor transformations and output in tsv format.

## Usage

```
java -jar TableExtractor <DescriptionFile> <ExcelFile>
```

## Available commands

### Input

`useSheets <names or regex>` list of names or regex for input sheets

`headerRow <num>` header row

`firstRow <num>` first row of data 

`sheetNameColumn <name>` put sheet name to given column

### Operations

`coalesce <col1>, <col2>... to <col>` extract first non null value to column

`concat <col1>, <col2>... to <col>` concatenate columns with " " to column

`dropNA <col1>, <col2>` drop rows if any of columns is null

`extract <pattern> from <col> to <col>` extract with regex to column 

`filter <col> eq <value>` or `filter <col> like <pattern>` filter row by value or regex

`put <value> to <col>` put static value to column

`rename <old1>: <new1>, <old2>: <new2>...` rename columns with mapping

### Output

`output <col1>, <col2>...` output with given columns and order
