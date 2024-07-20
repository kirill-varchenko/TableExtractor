package org.example.model

class Table {
    String sourceId
    String sheet
    String[] inColumns
    int headerRowIdx = 0
    int startRowIdx = 1
    Row row = new Row()

    def source(String sourceId) {
        this.sourceId = sourceId
    }

    def useSheet(String sheet) {
        this.sheet = sheet
    }

    def useColumns(String... columns) {
        inColumns = columns
    }

    def rename(Map<String, String> mapping) {
        row.operations << new Rename(mapping)
    }

    def headerRow(int rowNum) {
        headerRowIdx = rowNum - 1
    }

    def startRow(int rowNum) {
        startRowIdx = rowNum - 1
    }

    def rows(Closure closure) {
        closure.delegate = row
        closure.call()
    }

    @Override
    String toString() {
        return "Table[sourceId=$sourceId, sheet=$sheet, inColumns=$inColumns, startRowIdx=$startRowIdx, row=$row]"
    }
}
