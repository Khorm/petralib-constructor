package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Принимающие сообщения блоки. Активности либо воркфлоу
 */
@AllArgsConstructor
@Getter
public class LocalConsumerModel {
    private Long id;
    private String version;
    private String blockType;
    private String name;

    private Collection<ValueDto> inputModels;
    private Collection<ValueDto> outputModels;


}
