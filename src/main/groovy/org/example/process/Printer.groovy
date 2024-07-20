package org.example.process

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.QuoteMode

class Printer {
    CSVPrinter csvPrinter

    Printer(Appendable writer, String[] columns) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(columns)
                .setDelimiter('\t' as char)
                .setRecordSeparator('\n' as char)
                .setEscape('\\' as char)
                .setQuoteMode(QuoteMode.NONE)
                .build()
        csvPrinter = new CSVPrinter(writer, csvFormat)
    }

    def print(List<String> row) {
        csvPrinter.printRecord(row)
    }
}
