package org.example


import org.example.operations.*

class Main {
    static Extractor extractor
    static String[] output = []

    static void main(String[] args) {
        if (args.length != 2) {
            println "Usage: TableExtractor <DescriptionFile> <ExcelFile>"
            System.exit(1)
        }
        String scriptFile = args[0]
        String excelFile = args[1]

        extractor = new Extractor(excelFile)

        evaluateDSL(scriptFile)

        Printer printer = new Printer(System.out, output)

        try {
            extractor.extract(printer)
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage())
        }

    }

    static void evaluateDSL(String scriptFile) {
        def shell = new GroovyShell()
        def script = new File(scriptFile).text

        shell.setVariable("useSheets", extractor.&setSheets)
        shell.setVariable("sheetNameColumn", extractor.&setSheetNameColumn)
        shell.setVariable("headerRow", extractor.&setHeaderRow)
        shell.setVariable("firstRow", extractor.&setFirstRow)

        shell.setVariable("extract", this.&extract)
        shell.setVariable("filter", this.&filter)
        shell.setVariable("dropNA", this.&dropNA)
        shell.setVariable("rename", this.&rename)
        shell.setVariable("coalesce", this.&coalesce)
        shell.setVariable("concat", this.&concat)
        shell.setVariable("put", this.&put)

        shell.setVariable("output", this.&output)

        shell.evaluate(script)
    }

    static Extract extract(String pattern) {
        Extract extract = new Extract(pattern);
        extractor.add(extract)
        return extract
    }

    static Filter filter(String column) {
        Filter filter = new Filter(column);
        extractor.add(filter)
        return filter
    }

    static DropNA dropNA(String... columns) {
        DropNA dropNA = new DropNA(columns);
        extractor.add(dropNA)
        return dropNA
    }

    static Rename rename(Map<String, String> mapping) {
        Rename rename = new Rename(mapping);
        extractor.add(rename)
        return rename
    }

    static Coalesce coalesce(String... columns) {
        Coalesce coalesce = new Coalesce(columns);
        extractor.add(coalesce)
        return coalesce
    }

    static Concat concat(String... columns) {
        Concat concat = new Concat(columns);
        extractor.add(concat)
        return concat
    }

    static Put put(String value) {
        Put put = new Put(value)
        extractor.add(put)
        return put
    }

    static void output(String... columns) {
        output = columns
    }
}