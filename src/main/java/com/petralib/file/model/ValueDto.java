package com.petralib.file.model;


import com.petralib.ctype.enums.Multiplicity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ValueDto {
    private Long id;
    private String name;
    private String multiplicity;

    public Multiplicity getMultiplicity() {
        return Multiplicity.valueOf(multiplicity);
    }
}
