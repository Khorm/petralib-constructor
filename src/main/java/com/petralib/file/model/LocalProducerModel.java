package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Это непосредственно workflow
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalProducerModel {
    private Long id;
    private String version;

    private String name;

    /**
     * Потребители внутри workflow
     */
    private Collection<RemoteConsumerModel> consumers;
    private ValuesCollectionModel exitValues;


}
