# TableExtractor

This project defines a Groovy-based DSL for extracting data from an Excel sheet with minor transformations and saving to a .tsv file.

## Usage

```
java -jar TableExtractor <TableDescriptionFile> <ExcelFile>
```

## Example table definition
```
table {
    source "mh5"
    
    useSheet "Лист1"
    
    useColumns "Organization", "Gender", "Age", "DOB", "Children_age", "DOO", "DOC",
               "MH_ID", "Vaccination_status", "Vaccine_naмe", "DOLV", "Note", "Region", "City",
               "DOD", "Ct_SC2_LмV", "Speciмen_type"
    
    startRow 4
                
    rename Ct_SC2_LмV : "ct", DOD : "delivery_date", DOO : "disease_date", DOC : "collection_date", 
           MH_ID : "lmv_id", Organization : "organization", City : "city", Gender : "patient_sex", 
           Note : "comment", Region : "subject", Speciмen_type : "specimen"
           
    rows {
        concat "Vaccination_status", "Vaccine_naмe", "DOLV" to "vaccination"
        coalesce "DOB", "Age", "Children_age" to "patient_age"
    }
}
```