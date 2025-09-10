package com.petralib.file.model;


import com.petralib.ctype.enums.Multiplicity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SourceInputVariableModel {
    Long sourceVariable;
    Long currentBlockVariable;
    String extractionString;
    String sourceValueName;
    String sourceValueMultiplicity;

    public Long getSourceVariable() {
        return sourceVariable;
    }

    public Long getCurrentBlockVariable() {
        return currentBlockVariable;
    }

    public String getExtractionString() {
        return extractionString;
    }

    public String getSourceValueName() {
        return sourceValueName;
    }

    public Multiplicity getSourceValueMultiplicity() {
        return Multiplicity.valueOf(sourceValueMultiplicity);
    }
}
