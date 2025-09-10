package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalProducerModel {
    private Long id;
    private String version;
    private String name;
    private Collection<RemoteConsumerModel> consumers;
    private Collection<ValueLoaderModel> lastWorkflowBlockValueParser;
    private Integer lastWorkflowBlockValuesCount;
}
