package org.example

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.QuoteMode

class Printer {
    CSVPrinter csvPrinter
    String[] columns

    Printer(Appendable writer, String[] columns) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(columns)
                .setDelimiter('\t' as char)
                .setRecordSeparator('\n' as char)
                .setEscape('\\' as char)
                .setQuoteMode(QuoteMode.NONE)
                .build()
        csvPrinter = new CSVPrinter(writer, csvFormat)
        this.columns = columns
    }

    def print(Map<String, String> entries) {
        List<String> row = columns.collect { col ->
            entries.getOrDefault(col, "")?.strip() ?: ""
        }
        csvPrinter.printRecord(row)
    }
}
