package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class LocalSourceModel {
    private Long id;
    private String version;
    private String name;
    private Collection<ValueDto> outputModels;

    public Long getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public Collection<ValueDto> getOutputModels() {
        return outputModels;
    }
}
