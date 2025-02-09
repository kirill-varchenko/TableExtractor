package org.example


import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class Extractor {
    static final int rawNumBound = 2050

    String filename
    String[] sheets
    String sheetNameColumn
    int headerIdx = 0
    int firstIdx = 1
    List<Operation> operations = new ArrayList<>()

    Extractor(String filename) {
        this.filename = filename
    }

    def extract(Printer printer) {
        new File(filename).withInputStream { fileInputStream ->

            Workbook workbook = new XSSFWorkbook(fileInputStream)

            if (sheets) {
                workbook.sheetIterator().each {
                    if (sheetMatches(it.getSheetName())) {
                        processSheet(it, printer)
                    }
                }
            } else {
                Sheet sheet = workbook.getSheetAt(0)
                processSheet(sheet, printer)
            }
        }
    }

    def processSheet(Sheet sheet, Printer printer) {
        Row headerRow = sheet.getRow(headerIdx)
        Map<String, Integer> headers = headerRow.findAll { cell -> cell.getCellType() == CellType.STRING }
                .collectEntries { cell -> [(cell.getStringCellValue()): cell.getColumnIndex()] }

        sheet.each { row ->
            if (row.getRowNum() >= firstIdx) {
                Map<String, String> entries = headers.collectEntries { entry -> [(entry.key): parseCell(row.getCell(entry.value))] }
                if (sheetNameColumn) {
                    entries[sheetNameColumn] = sheet.getSheetName()
                }

                def result = operations.inject(entries) { acc, operation ->
                    acc ? operation.apply(acc) : null
                }
                if (result)
                    printer.print(result)
            }
        }
    }

    static String parseCell(Cell cell) {
        if (cell == null)
            return null
        if (cell.cellType == CellType.STRING)
            return cell.getStringCellValue()
        if (cell.cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                if (cell instanceof XSSFCell) {
                    return fixDate(cell)
                }
                return cell.toString()
            }
            double value = cell.getNumericCellValue()
            return (value % 1 == 0) ? String.valueOf((int) value) : String.valueOf(value)
        }
        return cell.toString()
    }

    static String fixDate(XSSFCell cell) {
        try {
            int rawNumber = (int) Double.parseDouble(cell.getRawValue())
            if (rawNumber < rawNumBound) {
                return String.valueOf(rawNumber)
            }
        } catch (NumberFormatException ignored) {
        }
        return cell.getLocalDateTimeCellValue().toLocalDate().toString()
    }

    def setHeaderRow(int rowNum) {
        headerIdx = rowNum - 1
    }

    def setFirstRow(int rowNum) {
        firstIdx = rowNum - 1
    }

    def add(Operation operation) {
        operations.add(operation)
    }

    boolean sheetMatches(String sheet) {
        for (String pat : sheets) {
            if (sheet ==~ pat)
                return true
        }
        return false
    }
}