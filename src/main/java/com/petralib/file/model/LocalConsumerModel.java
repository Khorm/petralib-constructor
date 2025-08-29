package com.petralib.file.model;

import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * Принимающие сообщения блоки. НАктивности либо воркфлоу
 */
@AllArgsConstructor
public class LocalConsumerModel {
    private Long id;
    private String version;

    private String blockType;
    private String name;

    private Collection<ValueLoaderModel> valueLoaders;
    //количество загружаемых переменных при инициализации. даже пустые считаются
    private Integer valuesCount;

    private Collection<ValueDto> outerValues;

    public Long getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getBlockType() {
        return blockType;
    }

    public String getName() {
        return name;
    }

    public Collection<ValueLoaderModel> getValueLoaders() {
        return valueLoaders;
    }

    public Integer getValuesCount() {
        return valuesCount;
    }

    public Collection<ValueDto> getOuterValues() {
        return outerValues;
    }
}
