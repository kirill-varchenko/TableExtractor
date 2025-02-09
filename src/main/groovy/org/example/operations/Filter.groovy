package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Filter implements Operation {
    String column
    String eq
    String like

    Filter(String column) {
        this.column = column
    }

    def eq(String eq) {
        this.eq = eq
    }

    def like(String like) {
        this.like = like
    }

    @Override
    def apply(Map<String, String> entries) {
        if (eq) {
            return entries[column] == eq ? entries : null
        }
        if (like) {
            return entries[column] ==~ like ? entries : null
        }
        return entries
    }
}
