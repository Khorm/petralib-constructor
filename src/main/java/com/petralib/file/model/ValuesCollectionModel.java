package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public class ValuesCollectionModel {
    private Collection<ValueModel> values;
    private Integer valueCount;
}
