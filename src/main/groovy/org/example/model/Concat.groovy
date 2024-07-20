package org.example.model

class Concat implements RowOperation {
    String[] cols
    String target

    Concat(String[] cols) {
        this.cols = cols
    }

    void to(String target) {
        this.target = target
    }

    @Override
    def apply(Map<String, String> entries) {
        String res = cols.findAll { col -> col in entries }.collect { col -> entries[col] }.join(" ")
        if (res != null && !res.blank)
            entries[target] = res
    }

    @Override
    String toString() {
        return "Concat[cols=${Arrays.toString(cols)}, target=$target]"
    }
}
