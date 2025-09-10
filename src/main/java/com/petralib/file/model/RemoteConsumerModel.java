package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RemoteConsumerModel {
    private Long id;
    private String version;
    private String serviceName;
}
