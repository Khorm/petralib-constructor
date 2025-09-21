package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalProducerModel {
    //айди продюсера - workflow
    private Long id;

    //версия продюсера
    private String version;

    //имя продюсера
    private String name;

    //удаленные блоки в этом продюсере
    private Collection<RemoteConsumerModel> consumers;

    //парсер выходных данных для продюсера
    private Collection<ValueLoaderModel> lastWorkflowBlockValueParser;
//    private Integer lastWorkflowBlockValuesCount;


}
