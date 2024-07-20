package org.example.model

class Rename implements RowOperation {
    Map<String, String> mapping

    Rename(Map<String, String> mapping) {
        this.mapping = mapping
    }

    @Override
    def apply(Map<String, String> entries) {
        mapping.each { rename ->
            if (rename.key in entries) {
                String value = entries[rename.key]
                entries.remove(rename.key)
                entries[rename.value] = value
            }
        }
    }

    @Override
    String toString() {
        return 'Rename' + mapping
    }
}
