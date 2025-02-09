package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Coalesce implements Operation {
    String[] columns
    String destination

    Coalesce(String[] columns) {
        this.columns = columns
    }

    void to(String destination) {
        this.destination = destination
    }

    @Override
    def apply(Map<String, String> entries) {
        String res = columns.find { col -> col in entries && !entries[col].blank }
        entries[destination] = res != null && !res.blank ? entries[res] : null
        return entries
    }

}
