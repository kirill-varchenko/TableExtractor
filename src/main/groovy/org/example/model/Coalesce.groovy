package org.example.model

class Coalesce implements RowOperation {
    String[] cols
    String target

    Coalesce(String[] cols) {
        this.cols = cols
    }

    void to(String target) {
        this.target = target
    }

    @Override
    def apply(Map<String, String> entries) {
        String res = cols.find { col -> col in entries && !entries[col].blank }
        if (res != null && !res.blank)
            entries[target] = entries[res]
    }


    @Override
    String toString() {
        return "Coalesce[cols=${Arrays.toString(cols)}, target=$target]"
    }
}
