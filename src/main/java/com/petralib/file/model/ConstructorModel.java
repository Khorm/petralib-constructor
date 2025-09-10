package com.petralib.file.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
public class ConstructorModel {
    private Collection<LocalConsumerModel> consumers;
    private Collection<LocalSourceModel> sources;
    private Collection<LocalProducerModel> producers;
}
