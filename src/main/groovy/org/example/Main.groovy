package org.example

import groovy.util.logging.Log4j2
import org.example.model.Table
import org.example.process.Extractor

@Log4j2
class Main {
    static void main(String[] args) {
        if (args.length != 2) {
            println "Usage: TableExtractor <TableDescriptionFile> <ExcelFile>"
            System.exit(1)
        }
        String scriptFile = args[0]
        String excelFile = args[1]

        Table table = evaluateDSL(scriptFile)
        Extractor extractor = new Extractor()
        try {
            extractor.extract(excelFile, table)
        } catch (RuntimeException ex) {
            log.error(ex.getMessage())
        }
    }

    static Table table(Closure closure) {
        Table table = new Table()
        closure.delegate = table
        closure.call()
        return table
    }

    static Table evaluateDSL(String scriptFile) {
        def shell = new GroovyShell()
        def script = new File(scriptFile).text

        shell.setVariable("table", this.&table)

        return shell.evaluate(script) as Table
    }
}