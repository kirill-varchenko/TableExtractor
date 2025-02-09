package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Rename implements Operation {
    Map<String, String> mapping

    Rename(Map<String, String> mapping) {
        this.mapping = mapping
    }

    @Override
    def apply(Map<String, String> entries) {
        return entries.collectEntries { key, value ->
            [(mapping.getOrDefault(key, key)): value]
        }
    }
}
