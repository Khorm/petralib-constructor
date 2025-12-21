package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalSourceModel {
    private Long id;
    private String version;
    private String name;
    private Collection<ValueDto> inputModels;
    private ValueDto outputModels;
}
