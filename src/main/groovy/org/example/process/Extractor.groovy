package org.example.process

import groovy.util.logging.Log4j2
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.example.model.Table

@Log4j2
class Extractor {
    static final String[] outColumns = ["source_id", "lmv_id", "organization", "collection_date", "subject", "city", "patient_sex",
                                        "patient_age", "disease_date", "comment", "delivery_date", "vaccination", "ct", "specimen"]
    static final int rawNumBound = 2050

    def extract(String filename, Table table) {
        log.info("Extracting: {}", filename)
        if (table.sourceId == null) {
            throw new RuntimeException("SourceId is not set")
        }
        String outFilename = "${table.sourceId}.tsv"

        new File(filename).withInputStream { fileInputStream ->
            new File(outFilename).withWriter { writer ->
                Printer printer = new Printer(writer, outColumns)

                Workbook workbook = new XSSFWorkbook(fileInputStream)

                Sheet sheet = table.sheet != null ? workbook.getSheet(table.sheet) : workbook.getSheetAt(0)

                Row headerRow = sheet.getRow(table.headerRowIdx)

                Map<String, Integer> headers = headerRow.findAll { cell -> cell.getCellType() == CellType.STRING }
                        .collectEntries { cell -> [(cell.getStringCellValue()): cell.getColumnIndex()] }

                if (!(headers.keySet() containsAll table.inColumns)) {
                    throw new RuntimeException("Missing columns: " + (table.inColumns - headers.keySet()))
                }

                sheet.each { row ->
                    if (row.getRowNum() >= table.startRowIdx) {
                        Map<String, String> entries = headers.collectEntries { entry -> [(entry.key): parseCell(row.getCell(entry.value) as XSSFCell)] }

                        table.row.applyOperations(entries)
                        entries["source_id"] = table.sourceId

                        List<String> rec = outColumns.collect { col ->
                            entries.getOrDefault(col, "").strip()
                        }

                        printer.print(rec)
                    }
                }
            }
        }
        log.info("Output: {}", outFilename)
    }

    static String parseCell(XSSFCell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case CellType.STRING:
                    return cell.getStringCellValue()
                case CellType.NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        try {
                            int rawNumber = (int) Double.parseDouble(cell.getRawValue())
                            if (rawNumber < rawNumBound) {
                                log.debug("Wrong date format: {}", cell.getAddress())
                                return String.valueOf(rawNumber)
                            }
                        } catch (NumberFormatException ignored) {

                        }
                        return cell.getLocalDateTimeCellValue().toLocalDate().toString()
                    } else {
                        return cell.getRawValue()
                    }
                    break
                default:
                    return cell.getRawValue()
            }
        } else {
            return ""
        }
    }

}
