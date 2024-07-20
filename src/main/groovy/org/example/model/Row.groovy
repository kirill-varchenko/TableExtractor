package org.example.model

class Row {
    List<RowOperation> operations = new ArrayList<>()

    def rename(Map<String, String> mapping) {
        operations << new Rename(mapping)
    }

    Concat concat(String... cols) {
        Concat concat = new Concat(cols)
        operations << concat
        return concat
    }

    Coalesce coalesce(String... cols) {
        Coalesce coalesce = new Coalesce(cols)
        operations << coalesce
        return coalesce
    }

    void applyOperations(Map<String, String> entries) {
        operations.each {operation -> operation.apply(entries)}
    }

    @Override
    String toString() {
        return "Row" + operations
    }
}
