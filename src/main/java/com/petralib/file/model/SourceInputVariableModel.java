package com.petralib.file.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SourceInputVariableModel {
    private Long sourceVariable;
    private String sourceValueName;
    private String sourceValueMultiplicity;

    private Long currentBlockVariable;
    private String extractionString;
}
