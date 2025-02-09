package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class DropNA implements Operation {
    String[] columns

    DropNA(String[] columns) {
        this.columns = columns
    }

    @Override
    def apply(Map<String, String> entries) {
        columns.any { key -> entries[key] == null } ? null : entries
    }
}
