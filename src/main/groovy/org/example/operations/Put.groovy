package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Put implements Operation {
    String value
    String destination

    Put(String value) {
        this.value = value
    }

    def to(String destination) {
        this.destination = destination
    }

    @Override
    def apply(Map<String, String> entries) {
        entries[destination] = value
        return entries
    }
}
