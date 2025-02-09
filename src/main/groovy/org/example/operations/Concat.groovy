package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Concat implements Operation {
    String[] columns
    String destination

    Concat(String[] columns) {
        this.columns = columns
    }

    void to(String destination) {
        this.destination = destination
    }

    @Override
    def apply(Map<String, String> entries) {
        String res = columns.findAll { col -> col in entries }.collect { col -> entries[col] }.join(" ")
        entries[destination] = res != null && !res.blank ? res : null
        return entries
    }
}
