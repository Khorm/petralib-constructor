package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RemoteConsumerModel {
    private Long id;
    private String version;
    private String serviceName;

    //лоадеры для входящих значений
    private Collection<ValueLoaderModel> valueLoaders;

}
