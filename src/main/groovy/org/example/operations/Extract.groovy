package org.example.operations

import groovy.transform.ToString
import org.example.Operation

@ToString
class Extract implements Operation {
    String pattern
    String source
    String destination

    Extract(String pattern) {
        this.pattern = pattern
    }

    def from(String source) {
        this.source = source
        return this
    }

    def to(String destination) {
        this.destination = destination
    }

    @Override
    def apply(Map<String, String> entries) {
        def matcher = entries[source] =~ pattern
        entries[destination] = matcher ? matcher[0][1] : null
        return entries
    }
}
