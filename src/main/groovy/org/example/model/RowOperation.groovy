package org.example.model

interface RowOperation {
    def apply(Map<String, String> entries)
}