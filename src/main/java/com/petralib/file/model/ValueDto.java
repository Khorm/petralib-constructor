package com.petralib.file.model;


import com.petralib.ctype.enums.Multiplicity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueDto {
    private Long id;
    private String name;
    private String multiplicity;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Multiplicity getMultiplicity() {
        return Multiplicity.valueOf(multiplicity);
    }
}
